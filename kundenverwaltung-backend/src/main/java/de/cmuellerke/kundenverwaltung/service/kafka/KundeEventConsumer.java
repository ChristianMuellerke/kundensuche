package de.cmuellerke.kundenverwaltung.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KundeEventConsumer {
	@KafkaListener(topics = "kunde-neu", groupId = "foo")
	public void listenGroupFoo(String message) {
	    System.out.println("Received Message in group foo: " + message);
	}
}
