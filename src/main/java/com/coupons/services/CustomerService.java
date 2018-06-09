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
import com.coupons.business_delegate.BusinessDelegate;
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
	public Object purchaseCoupon(Coupon coupon) {
		CustomerFacade  facade = (CustomerFacade) httpRequest.getSession().getAttribute("facade");
		try {
			System.out.println("Customer service purchesCoupon works");
			facade.purchaseCoupon(coupon);
			System.out.println("Facade.PurchesCoupon works");
			BusinessDelegate.BusinessDelegate.storeIncome(facade.getCurrentCustomerInfo().getCustName(),
					"CUSTOMER_PURCHASE", coupon.getPrice());
			return new ApplicationResponse(0, "Coupon purchased successfully.");
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());
		}
	}
	
	
	@GET
	@Path("coupon")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getAllCustomerCoupons() {
		CustomerFacade facade = (CustomerFacade) httpRequest.getSession().getAttribute("facade");
		try {
			return facade.getAllPurchasedCoupons();
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());
		}
	}

	@GET
	@Path("coupon/price")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getCouponByPrice(@QueryParam("price") double price) {
		CustomerFacade facade = (CustomerFacade) httpRequest.getSession().getAttribute("facade");
		try {
			return facade.getAllPurchasedCouponsByPrice(price);
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());
		}
	}

	@GET
	@Path("coupon/type")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getCouponByType(@QueryParam("type") CouponType type) {
		CustomerFacade facade = (CustomerFacade) httpRequest.getSession().getAttribute("facade");
		try {
			return facade.getAllPurchasedCouponsByType(type);
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());
		}
	}
	
	@POST
	@Path("register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object register()
	{
		//Unimplemented yet
		
		return new ApplicationResponse(0, "Customer successfully registered.");
	}
	
	@GET
	@Path("allCoupons")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getAllCoupons()
	{
		CustomerFacade facade = (CustomerFacade) httpRequest.getSession().getAttribute("facade");
		try {
			return facade.showAllCoupons();
		}
		catch(MyException e)
		{
			return new ApplicationResponse(1, e.getMessage());
		}
	}

}
