package de.cmuellerke.kundenverwaltung.batch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TenantPartitioner implements Partitioner {

	private static final String DEFAULT_KEY_NAME = "tenantId";
	private static final String PARTITION_KEY = "partition";
	
	private Integer[] tenants = new Integer[] {};
	private String keyName = DEFAULT_KEY_NAME;

	public void setTenants(Integer[] tenants) {
		this.tenants = tenants;
	}
	
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	
	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		Map<String, ExecutionContext> map = new HashMap<String, ExecutionContext>(gridSize);
		
		int i = 0, k = 1;
		
		for (Integer tenant: tenants) {
			log.info("[] creating partition {}", tenant, i);
			ExecutionContext context = new ExecutionContext();
			context.putInt(keyName, tenant);
			map.put(PARTITION_KEY + i, context);
			i++;
		}
		
		return map;
	}

}
