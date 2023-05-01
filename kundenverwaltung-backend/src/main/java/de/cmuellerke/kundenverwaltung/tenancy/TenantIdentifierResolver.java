package de.cmuellerke.kundenverwaltung.tenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {
	
	// see sample under https://medium.com/deviniti-technology-driven-blog/implementing-multitenancy-architecture-spring-boot-jpa-hibernate-flyway-8fb19b312a10
	
	@Override
	public String resolveCurrentTenantIdentifier() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //If session attribute exists returns tenantId saved on the session
        
        /*
         * Bei /signIn und /login Requests ist die TenantId noch NULL, da diese nicht gesetzt werden
         * Danach, bei normalen Requests ist der TenantContext dann gefüllt
         * 
         * Ausserdem scheint das @Component nicht zu funktionieren? Wir brauchen aber 
         * 
         * 
         * Irgendwas stimmt mit dem Lifecycle nicht. Der AuthTokenFilter wird auch bei signIn und auth ausgeführt - und der ruft direkt in der DB an - hat aber keinen TenantContext dann gesetzt
         */
        
        log.debug("TenantId from TenantContext: " + TenantContext.getTenantId());
        
        if (TenantContext.getTenantId() == null) {
        	return "DEFAULT";
        } 
        	
        return TenantContext.getTenantId();
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}

		return null;
	}
}