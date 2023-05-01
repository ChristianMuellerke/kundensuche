package de.cmuellerke.kundenverwaltung.service.kafka;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import de.cmuellerke.kundenverwaltung.payload.customer.KundeDTO;

@Component
public class KundeEventPublisher {
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void sendMessage(String message) {
	     CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("kunde-neu", message);
	     future.whenComplete((result, ex) -> {
	         if (ex == null) {
	             System.out.println("Sent message=[" + message + 
	                 "] with offset=[" + result.getRecordMetadata().offset() + "]");
	         } else {
	             System.out.println("Unable to send message=[" + 
	                 message + "] due to : " + ex.getMessage());
	         }
	     });
	}
	
	public void versendeKundeAngelegt(KundeDTO kunde) {
		sendMessage("Neuer Kunde");
	}
	
	
}
