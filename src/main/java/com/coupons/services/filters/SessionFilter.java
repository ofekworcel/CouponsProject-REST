package com.coupons.services.filters;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.coupons.annotations.SessionFilterAnnotation;
import com.coupons.utils.other.ApplicationResponse;

import Facade.AdminFacade;
import Facade.CompanyFacade;
import Facade.CustomerFacade;

@Provider
@SessionFilterAnnotation
public class SessionFilter implements ContainerRequestFilter {

	@Context
	private HttpServletRequest httpRequest;
	
	
	@Override
	public void filter(ContainerRequestContext request) throws IOException {
		HttpSession session = httpRequest.getSession();
		String uri = request.getUriInfo().getPath();
		
		if(uri.contains("AdminService")) {
			if(session.getAttribute("facade") == null || !(session.getAttribute("facade") instanceof AdminFacade)) {
				request.abortWith(Response.status(Response.Status.UNAUTHORIZED).
						entity(new ApplicationResponse(1, "You need to login as admin to use admin services.")).build());
			}
		} else if(uri.contains("CompanyService")) {
			if(session.getAttribute("facade") == null || !(session.getAttribute("facade") instanceof CompanyFacade)) {
				request.abortWith(Response.status(Response.Status.UNAUTHORIZED).
						entity(new ApplicationResponse(1, "You need to login as company to use company services.")).build());
			}
		} else if(uri.contains("CustomerService")) {
			if(session.getAttribute("facade") == null || !(session.getAttribute("facade") instanceof CustomerFacade)) {
				request.abortWith(Response.status(Response.Status.UNAUTHORIZED).
						entity(new ApplicationResponse(1, "You need to login as customer to use customer services.")).build());
			}
		} 
		
		
		
	}

	

}
