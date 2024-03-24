package de.cmuellerke.poc.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TenantNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public TenantNotFoundException() {
        super();
    }
    
    public TenantNotFoundException(String message) {
        super(message);
    }
}
