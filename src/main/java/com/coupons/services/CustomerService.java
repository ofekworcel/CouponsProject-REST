package com.coupons.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.coupons.annotations.SessionFilterAnnotation;
import com.coupons.utils.classes.UserInfo;
import com.coupons.utils.other.ApplicationResponse;

import Facade.ClientType;
import Facade.CustomerFacade;
import JavaBeans.Coupon;
import JavaBeans.CouponType;
import Program.CouponSystemSingleton;
import Utilities.MyException;

@Path("CustomerService")
public class CustomerService {

	@Context
	private HttpServletRequest httpRequest;

	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object login(UserInfo info) {
		try {
			CustomerFacade facade = (CustomerFacade) CouponSystemSingleton.getInstance().login(info.getUsername(),
					info.getPassword(), ClientType.CUSTOMER);
			if (facade == null)
				return Response.status(Status.UNAUTHORIZED)
						.entity(new ApplicationResponse(0, "The information you have provided is incorrect."))
						.type(MediaType.APPLICATION_JSON).build();
			httpRequest.getSession().invalidate();
			httpRequest.getSession().setAttribute("facade", facade);
			// return new ApplicationResponse(0, "Customer has logged in successfully");
			return Response.status(Status.OK).entity(new ApplicationResponse(1, "Customer has logged in successfully."))
					.type(MediaType.APPLICATION_JSON).build();
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());
		}

	}

	@POST
	@Path("purchaseCoupon")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object purchaseCoupon(Coupon coupon) {
		CustomerFacade facade = (CustomerFacade) httpRequest.getSession().getAttribute("facade");

		try {
			facade.purchaseCoupon(coupon);
			return new ApplicationResponse(0, "");
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());
		}

	}

	@GET
	@Path("coupon")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getAllPurchasedCoupons() {
		try {

			CustomerFacade facade = (CustomerFacade) httpRequest.getSession().getAttribute("facade");
			return facade.getAllPurchasedCoupons();
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());
		}

	}

	@GET
	@Path("coupon/{couponType}")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getAllPurchasedCouponsByType(@PathParam("couponType") CouponType type) {
		try {

			CustomerFacade facade = (CustomerFacade) httpRequest.getSession().getAttribute("facade");
			return facade.getAllPurchasedCouponsByType(type);
		} catch (MyException e) {
			return new ApplicationResponse(1, "Please try again");
		}
	}

	@GET
	@Path("coupon")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getAllPurchasedCouponsByPrice(@QueryParam("price")double price) {
		try {

			CustomerFacade facade = (CustomerFacade) httpRequest.getSession().getAttribute("facade");
			return facade.getAllPurchasedCouponsByPrice(price);
		} catch (MyException e) {
			return new ApplicationResponse(1, "Please try again");
		}

	}

}
