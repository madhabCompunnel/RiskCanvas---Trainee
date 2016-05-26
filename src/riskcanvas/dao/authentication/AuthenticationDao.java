//interface for data access layer
package riskcanvas.dao.authentication;

import riskcanvas.dao.DatabaseConnection;
import riskcanvas.model.Password;

public interface AuthenticationDao 
{

	//Change and reset password
	Boolean changePassword(String userName,String alfTicket, Password password, DatabaseConnection conndata);

}
