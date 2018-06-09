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

import Facade.AdminFacade;
import Facade.ClientType;
import JavaBeans.Company;
import JavaBeans.Customer;
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
			httpRequest.getSession().invalidate();
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
	@SessionFilterAnnotation
	public Object createCompany(Company company) {
		AdminFacade facade = (AdminFacade) httpRequest.getSession().getAttribute("facade");
		try {
			facade.addCompany(company);
			return Response.status(Status.OK)
					.entity(new ApplicationResponse(0, "Company has been successfully created."))
					.type(MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(new ApplicationResponse(0, e.getMessage()))
					.type(MediaType.APPLICATION_JSON).build();
		}
	}

	@GET
	@Path("company")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getCompanies() {
		AdminFacade facade = (AdminFacade) httpRequest.getSession().getAttribute("facade");
		try {
			return facade.getAllCompanies();
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());
		}
	}

	@GET
	@Path("company/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getCompanyByID(@PathParam("id") long id) {
		AdminFacade facade = (AdminFacade) httpRequest.getSession().getAttribute("facade");
		try {
			return facade.getCompany(id);
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());
		}
	}

	@PUT
	@Path("company")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object updateCompany(Company company) {
		AdminFacade facade = (AdminFacade) httpRequest.getSession().getAttribute("facade");
		try {
			facade.updateCompany(company);
			return new ApplicationResponse(0, "Company has been successfully updated.");
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());
		}
	}

	@DELETE
	@Path("company/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object deleteCompany(@PathParam("id") long id) {
		AdminFacade facade = (AdminFacade) httpRequest.getSession().getAttribute("facade");
		try {
			facade.removeCompany(id);
			return new ApplicationResponse(0, "Company has been successfully removed.");
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());
		}
	}

	@POST
	@Path("customer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object createCustomer(Customer customer) {
		AdminFacade facade = (AdminFacade) httpRequest.getSession().getAttribute("facade");
		try {
			System.out.println("create customer function activated");
			facade.addCustomer(customer);
			return Response.status(Status.OK)
					.entity(new ApplicationResponse(0, "Customer has been successfully created."))
					.type(MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(new ApplicationResponse(0, e.getMessage()))
					.type(MediaType.APPLICATION_JSON).build();
		}
	}

	@GET
	@Path("customer")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getCustomers() {
		AdminFacade facade = (AdminFacade) httpRequest.getSession().getAttribute("facade");
		try {
			return facade.getAllCustomer();
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());
		}
	}
	
	@GET
	@Path("customerid/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getCustomerByID(@PathParam("id") long id) {
		AdminFacade facade = (AdminFacade) httpRequest.getSession().getAttribute("facade");
		try {
			return facade.getCustomer(id);
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());
		}
	}
	
	@DELETE
	@Path("customer/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object deleteCustomer(@PathParam("id") long id)
	{
		AdminFacade facade = (AdminFacade) httpRequest.getSession().getAttribute("facade");
		try {
			facade.deleteCustomer(id);
			return new ApplicationResponse(0, "Customer has been successfully removed.");
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());
		}
			
	}
	
	@PUT
	@Path("customer")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object updateCustomer(Customer customer)
	{
		AdminFacade facade = (AdminFacade) httpRequest.getSession().getAttribute("facade");
		try {
			facade.updateCustomer(customer);
			return new ApplicationResponse(0, "Customer has been successfully updated.");
		} catch (MyException e) {
			return new ApplicationResponse(1, e.getMessage());

		}
	}

	@GET
	@Path("income")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object viewAllIncome() {
		try {
			return BusinessDelegate.BusinessDelegate.viewAllIncome();
		} catch (MyException e) {
			return new ApplicationResponse(1, "There has been a problem retrieving income information.");
		}
	}
	
	@GET
	@Path("income/company")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object viewIncomeByCompany(@QueryParam("name") String name) {
		try {
			return BusinessDelegate.BusinessDelegate.viewIncomeByCompany(name);
		} catch (MyException e) {
			return new ApplicationResponse(1, "There has been a problem retrieving income information of specified company.");
		}
	}
	
	@GET
	@Path("income/customer")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object viewIncomeByCustomer(@QueryParam("name") String name) {
		try {
			return BusinessDelegate.BusinessDelegate.viewIncomeByCustomer(name);
		} catch (MyException e) {
			return new ApplicationResponse(1, "There has been a problem retrieving income information.");
		}
	}

	
}
