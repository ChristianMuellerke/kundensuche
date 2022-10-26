package de.cmuellerke.kundenverwaltung.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.cmuellerke.kundenverwaltung.payload.MessageResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/test")
@Slf4j
public class TestController {

	@GetMapping("/hello")
	public ResponseEntity<MessageResponse> getMessage() {
		
        log.trace("This is a TRACE level message");
        log.debug("This is a DEBUG level message");
        log.info("This is an INFO level message");
        log.warn("This is a WARN level message");
        log.error("This is an ERROR level message");

		MessageResponse messageResponse = new MessageResponse("Hello World!");
		return ResponseEntity.ok(messageResponse);
	}
	
	@PostMapping("/reply")
	public ResponseEntity<MessageResponse> postMessage(@Valid @RequestBody MessageResponse message) {
		log.info("Message from Request: {}", message.getMessage());
		MessageResponse messageResponse = new MessageResponse("R=" + message.getMessage());
		return ResponseEntity.ok(messageResponse);
	}

}
