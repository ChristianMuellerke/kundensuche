package de.cmuellerke.kundenverwaltung.tenancy;

import java.io.IOException;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//public class TenantInterceptor implements HandlerInterceptor {
//@Component
public class TenantInterceptor implements WebRequestInterceptor {

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
//                             Object handler) throws IOException {
//        String requestURI = request.getRequestURI();
//        String tenantID = request.getHeader("X-TENANT-ID");
//        System.out.println("RequestURI " + requestURI + " Search for X-TENANT-ID  :: " + tenantID);
//        if (tenantID == null) {
//            response.getWriter().write("X-TENANT-ID not present in the Request Header");
//            response.setStatus(400);
//            return false;
//        }
//        TenantContext.setTenantInfo(tenantID);
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
//                           @Nullable ModelAndView modelAndView) throws Exception {
//        TenantContext.clear();
//    }

	@Override
	public void preHandle(WebRequest request) throws Exception {
		String tenantId = null;
		if (request.getHeader("X-TENANT-ID") != null) {
			tenantId = request.getHeader("X-TENANT-ID");
		}
		
		TenantContext.setTenantInfo(tenantId);
		
	}

	@Override
	public void postHandle(WebRequest request, ModelMap model) throws Exception {
		TenantContext.clear();
	}

	@Override
	public void afterCompletion(WebRequest request, Exception ex) throws Exception {
		// NOOP
	}
}
