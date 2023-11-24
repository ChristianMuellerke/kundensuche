package de.cmuellerke.poc.tenancy;

public class TenantContextAccess {

	public String getTenantId() {
		return TenantContext.getTenantId();
	}
}
