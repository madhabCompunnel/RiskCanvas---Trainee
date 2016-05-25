//interface for data access layer
package riskcanvas.dao.user;

import riskcanvas.dao.DatabaseConnection;
import riskcanvas.model.AddUser;

public interface UserDao {

	public Boolean AddUser(AddUser adduser,DatabaseConnection connection) ;//add user

	public Boolean EditUser(AddUser adduser, DatabaseConnection conndata,String pathUsername);//edit user
	
}
