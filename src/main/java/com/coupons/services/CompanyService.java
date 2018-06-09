package com.coupons.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.coupons.annotations.SessionFilterAnnotation;
import com.coupons.business_delegate.BusinessDelegate;
import com.coupons.utils.classes.UserInfo;
import com.coupons.utils.other.ApplicationResponse;

import Facade.ClientType;
import Facade.CompanyFacade;
import JavaBeans.Coupon;
import JavaBeans.CouponType;
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
	@SessionFilterAnnotation
	public Object createCoupon(Coupon coupon) {
		CompanyFacade facade = (CompanyFacade) httpRequest.getSession().getAttribute("facade");
		try {
			facade.addCoupon(coupon);
			BusinessDelegate.BusinessDelegate.storeIncome(facade.getCurrentCompanyInfo().getCompName(),
					"COMPANY_NEW_COUPON", 100);
			return Response.status(Status.OK)
					.entity(new ApplicationResponse(0, "Coupon has been created successfully."))
					.type(MediaType.APPLICATION_JSON).build();
		} catch (MyException e) {
			return Response.status(Status.BAD_REQUEST).entity(new ApplicationResponse(0, e.getMessage()))
					.type(MediaType.APPLICATION_JSON).build();
		}
	}

	@PUT
	@Path("coupon")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object updateCoupon(Coupon coupon) {
		System.out.println("updateCoupon in CompanyService works!");
		CompanyFacade facade = (CompanyFacade) httpRequest.getSession().getAttribute("facade");
		try {
			facade.updateCoupon(coupon);
			BusinessDelegate.BusinessDelegate.storeIncome(facade.getCurrentCompanyInfo().getCompName(), "COMPANY_UPDATE_COUPON", 10);
			return new ApplicationResponse(0, "Coupon updated successfully.");
		} catch (MyException e) {
			System.out.println("updateCoupon CompanyService dropped on sql exception");
			return new ApplicationResponse(1, e.getMessage());
		}
	}

	@DELETE
	@Path("coupon/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object deleteCoupon(@PathParam("id") long id) {
		CompanyFacade facade = (CompanyFacade) httpRequest.getSession().getAttribute("facade");
		try {
			facade.removeCoupon(id);
			return new ApplicationResponse(0, "Coupon removed successfully.");
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());
		}
	}

	@GET
	@Path("coupon")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getAllCompanyCoupons() {
		CompanyFacade facade = (CompanyFacade) httpRequest.getSession().getAttribute("facade");
		try {
			return facade.getAllCoupon();
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());
		}
	}

	@GET
	@Path("coupon/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getCouponById(@PathParam("id") long id) {
		CompanyFacade facade = (CompanyFacade) httpRequest.getSession().getAttribute("facade");
		try {
			return facade.getCoupon(id);
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());
		}
	}

	@GET
	@Path("coupon/type")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getCouponByType(@QueryParam("type") CouponType type) {
		CompanyFacade facade = (CompanyFacade) httpRequest.getSession().getAttribute("facade");
		try {
			return facade.getCouponbyType(type);
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());
		}
	}

}
