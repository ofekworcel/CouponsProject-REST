package com.coupons.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.coupon.utils.other.ApplicationResponse;
import com.coupons.utils.classes.UserInfo;

import Facade.AdminFacade;
import Facade.ClientType;
import JavaBeans.Company;
import Program.CouponSystemSingleton;
import Utilities.MyException;

@Path("AdminService")
public class AdminService {

	@Context
	private HttpServletRequest httpRequest;

	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object login(UserInfo info) {
		try {
			AdminFacade facade = (AdminFacade) CouponSystemSingleton.getInstance().login(info.getUsername(),
					info.getPassword(), ClientType.ADMIN);
			if (facade == null)
				return Response.status(Status.UNAUTHORIZED)
						.entity(new ApplicationResponse(0, "The information you have provided is incorrect."))
						.type(MediaType.APPLICATION_JSON).build();
			httpRequest.getSession().setAttribute("facade", facade);
			// return new ApplicationResponse(0, "Admin has logged in successfully");
			return Response.status(Status.OK).entity(new ApplicationResponse(0, "Admin has logged in successfully."))
					.type(MediaType.APPLICATION_JSON).build();
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());
		}

	}

	@POST
	@Path("company")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object createCompany(Company company) {
		AdminFacade facade = (AdminFacade) httpRequest.getSession().getAttribute("facade");
		try {
			facade.addCompany(company);
			return Response.status(Status.OK)
					.entity(new ApplicationResponse(0, "Company has been created successfully."))
					.type(MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(new ApplicationResponse(0, e.getMessage()))
					.type(MediaType.APPLICATION_JSON).build();
		}
	}
	
	@GET
	@Path("company")
	@Produces(MediaType.APPLICATION_JSON)
	public Object getCompanies() {
		AdminFacade facade = (AdminFacade) httpRequest.getSession().getAttribute("facade");
		return facade.getAllCompanies();
	}

}
