package riskcanvas.service;

import javax.servlet.http.HttpServletRequest;

import riskcanvas.model.Password;

public interface AuthenticatonService 
{
	boolean changePassword(String alfTicket,Password password,HttpServletRequest request);
}
