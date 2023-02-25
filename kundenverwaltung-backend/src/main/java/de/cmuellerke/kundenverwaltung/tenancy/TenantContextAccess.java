package de.cmuellerke.kundenverwaltung.tenancy;

public class TenantContextAccess {

	public String getTenantId() {
		return TenantContext.getTenantId();
	}
}
