package de.cmuellerke.demo.security;

import java.io.Serializable;

public class JwtRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7775501323752944482L;

	private String username;
	private String password;
	private String tenantId;
	
	//need default constructor for JSON Parsing
	public JwtRequest()
	{
		
	}

	public JwtRequest(String username, String password, String tenantId) {
		this.setUsername(username);
		this.setPassword(password);
		this.setTenantId(tenantId);
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
