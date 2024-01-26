package de.cmuellerke.kundenverwaltung.batch;

import java.rmi.UnexpectedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import de.cmuellerke.kundenverwaltung.models.CustomerEntity;
import de.cmuellerke.kundenverwaltung.payload.customer.CustomerDTO;
import de.cmuellerke.kundenverwaltung.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class BatchConfiguration {

	private static final int CHUNK_SIZE = 10;

	@Autowired
	private CustomerRepository customerRepository;

	@Bean
	@StepScope
	RepositoryItemReader<CustomerEntity> reader(@Value("#{stepExecutionContext[tenant]}") Integer tenant) {
		log.info("creating reader...");
		Map<String, Direction> sorts = new HashMap<>();
		sorts.put("ID", Direction.ASC);

		return new RepositoryItemReaderBuilder<CustomerEntity>().name("reader")
																.pageSize(10)
																.saveState(false)
																.sorts(sorts)
																.methodName("findByTenantId")
																.arguments(tenant)
																.repository(customerRepository)
																.build();
	}

	@Bean
	CustomerItemProcessor processor() {
		return new CustomerItemProcessor();
	}

	@Bean
	@StepScope
	ItemWriter<CustomerDTO> writer(@Value("#{stepExecutionContext[tenant]}") Integer tenant,
			@Value("#{stepExecutionContext.executionContext}") ExecutionContext ec,
			@Value("#{stepExecution}") StepExecution se) {
		return new CustomerItemWriter<CustomerDTO>(tenant, se);
	}

	@Bean
	@StepScope
	TenantPartitioner tenantPartitioner() {
		List<Integer> allTenants = customerRepository.getAllTenants();
		TenantPartitioner tenantPartitioner = new TenantPartitioner();
		Integer[] tenants = allTenants.toArray(new Integer[0]);
		tenantPartitioner.setTenants(tenants);
		return tenantPartitioner;
	}
	
	@Bean
	Job exportCustomersJob(JobRepository jobRepository, 
			Step step1, 
			JobCompletionNotificationListener listener,
			PlatformTransactionManager transactionManager) throws UnexpectedException {
		
		Flow flow = new FlowBuilder<Flow>("mainFlow")
				.start(partionedCopyStep(jobRepository, transactionManager))
				.build();

		// das muss nach Spring Boot verlagert werden denn es startet automatisch den Job?
		return new JobBuilder("exportCustomersJob", jobRepository)	.listener(listener)
																	.start(flow)
																	.build()
																	.preventRestart()
																	.build();
	}

	@Bean
	Step partionedCopyStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) 
	throws UnexpectedException {
		Step copyStep = new StepBuilder("copyPerTenantStep", jobRepository)
				.<CustomerEntity, CustomerDTO> chunk(CHUNK_SIZE, transactionManager)
				.reader(reader(null))
				.processor(processor())
				.writer(writer(null, null, null))
				.build();

		Flow flow = new FlowBuilder<Flow>("copyFlow")
				.start(copyStep)
				.build();
		
		Step flowStep = new StepBuilder("flowStep", jobRepository)
				.flow(flow)
				.build();
		
		return new StepBuilder("partionedCopyStep", jobRepository)
				.partitioner("copyForTenantStep", tenantPartitioner())
				.step(flowStep)
				.taskExecutor(taskExecutor())
				.gridSize(2)
				.build();
	}

	@Bean
	TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(12);
		taskExecutor.setCorePoolSize(10);
		taskExecutor.setQueueCapacity(1000);
		taskExecutor.afterPropertiesSet();
		return taskExecutor;
	}
}