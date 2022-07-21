package de.cmuellerke.demo.data.dto;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class ApplicationUser extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tenant;
	
	/**
	 * Calls the more complex constructor with all boolean arguments set to {@code true}.
	 */
	public ApplicationUser(String username, String password, String tenant, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, true, true, true, true, authorities);
		this.tenant = tenant;
	}
	
	public ApplicationUser(String username, String password, String tenant, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.tenant = tenant;
	}

	public String getTenant() {
		return tenant;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(tenant);
		return result;
	}

	/**
	 * Username & Tenant must be equal.
	 * @see User
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApplicationUser other = (ApplicationUser) obj;
		return Objects.equals(tenant, other.tenant);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getName()).append(" [");
		sb.append("Tenant=").append(this.getTenant()).append(", ");
		sb.append("Username=").append(this.getUsername()).append(", ");
		sb.append("Password=[PROTECTED], ");
		sb.append("Enabled=").append(this.isEnabled()).append(", ");
		sb.append("AccountNonExpired=").append(this.isAccountNonExpired()).append(", ");
		sb.append("credentialsNonExpired=").append(this.isCredentialsNonExpired()).append(", ");
		sb.append("AccountNonLocked=").append(this.isAccountNonLocked()).append(", ");
		sb.append("Granted Authorities=").append(this.getAuthorities()).append("]");
		return sb.toString();
	}
}
