package riskcanvas.service;

import javax.servlet.http.HttpServletRequest;

import riskcanvas.dao.authentication.AuthenticationDao;
import riskcanvas.model.Password;

public class AuthenticationServiceImpl 
{
	AuthenticationDao authenticationDao;
	public void setAuthenticationDao(AuthenticationDao authenticationDao)
	{
		this.authenticationDao=authenticationDao;
	}
	public boolean changePassword(String alfTicket,Password password,HttpServletRequest request)
	{
		return false;
	}
}
