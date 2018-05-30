package com.coupons.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.coupons.utils.other.ApplicationResponse;

import Facade.AdminFacade;
import Facade.ClientType;
import Facade.CompanyFacade;
import Facade.CouponClientFacade;
import Facade.CouponsShopFacade;
import Utilities.MyException;

@Path("UtilService")
public class UtilService {

	@Context
	private HttpServletRequest httpRequest;
	
	@GET
	@Path("session")
	@Produces(MediaType.APPLICATION_JSON)
	public Object getSessionInfo() {
		CouponClientFacade facade = (CouponClientFacade) httpRequest.getSession().getAttribute("facade");
		if(facade == null)
			return ClientType.GUEST;
		else if(facade instanceof AdminFacade)
			return ClientType.ADMIN;
		else if(facade instanceof CompanyFacade)
			return ClientType.COMPANY;
		else 
			return ClientType.CUSTOMER;
	}
	
	@GET
	@Path("shop")
	@Produces(MediaType.APPLICATION_JSON)
	public Object getShop() {
		try {
			return new CouponsShopFacade().getCoupons();
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());
		}
	}
	
	
	@GET
	@Path("logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Object logout() {
		httpRequest.getSession().invalidate();
		return new ApplicationResponse(0, "Logged out successfully.");
	}
	
}
