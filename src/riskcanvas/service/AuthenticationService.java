package riskcanvas.service;

import riskcanvas.dao.DatabaseConnection;
import riskcanvas.model.Password;

public interface AuthenticationService 
{
	boolean changePassword(String userName,String alfTicket, Password password, DatabaseConnection conndata);
}
