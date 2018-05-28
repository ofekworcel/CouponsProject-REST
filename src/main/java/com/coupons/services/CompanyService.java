package com.coupons.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.coupons.utils.classes.UserInfo;
import com.coupons.utils.other.ApplicationResponse;

import Facade.AdminFacade;
import Facade.ClientType;
import Facade.CompanyFacade;
import JavaBeans.Coupon;
import Program.CouponSystemSingleton;
import Utilities.MyException;

@Path("CompanyService")
public class CompanyService {

	@Context
	private HttpServletRequest httpRequest;
	
	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object login(UserInfo info) {
		try {
			CompanyFacade facade = (CompanyFacade) CouponSystemSingleton.getInstance().login(info.getUsername(),
					info.getPassword(), ClientType.COMPANY);
			if (facade == null)
				return Response.status(Status.UNAUTHORIZED)
						.entity(new ApplicationResponse(0, "The information you have provided is incorrect."))
						.type(MediaType.APPLICATION_JSON).build();
			httpRequest.getSession().invalidate();
			httpRequest.getSession().setAttribute("facade", facade);
			return Response.status(Status.OK).entity(new ApplicationResponse(0, "Company has logged in successfully."))
					.type(MediaType.APPLICATION_JSON).build();
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());
		}
	}
	
	@POST
	@Path("coupon")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object createCoupon(Coupon coupon) {
		CompanyFacade facade = (CompanyFacade) httpRequest.getSession().getAttribute("facade");
		try {
			facade.addCoupon(coupon);
			return Response.status(Status.OK)
					.entity(new ApplicationResponse(0, "Coupon has been created successfully."))
					.type(MediaType.APPLICATION_JSON).build();
		} catch (MyException e) {
			return Response.status(Status.BAD_REQUEST).entity(new ApplicationResponse(0, e.getMessage()))
					.type(MediaType.APPLICATION_JSON).build();
		}
	}
	
	
}
