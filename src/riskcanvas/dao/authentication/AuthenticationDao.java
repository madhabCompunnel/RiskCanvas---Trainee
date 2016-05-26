package riskcanvas.dao.authentication;

import riskcanvas.model.Password;

public interface AuthenticationDao 
{
	void changePassword(String alfTicket,Password password);

}
