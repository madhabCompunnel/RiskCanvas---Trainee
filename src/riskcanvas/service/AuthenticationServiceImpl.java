package riskcanvas.service;

import riskcanvas.dao.DatabaseConnection;
import riskcanvas.dao.authentication.AuthenticationDao;
import riskcanvas.model.Password;

public class AuthenticationServiceImpl implements AuthenticationService
{
	AuthenticationDao authenticationDao;
	public void setAuthenticationDao(AuthenticationDao authenticationDao)
	{
		this.authenticationDao=authenticationDao;
	}
	
	/**
	 * For reset or change password
	 */
	@Override
	public boolean changePassword(String userName,String alfTicket, Password password, DatabaseConnection conndata) {
		//input to dataLayer(AuthenticationDao implementation)and retrieving output result
		Boolean success=authenticationDao.changePassword(userName,alfTicket,password,conndata);
		return success;
	}
	
}
