package riskcanvas.dao.user;

import riskcanvas.dao.DatabaseConnection;
import riskcanvas.model.AddUser;

public interface UserDao {

	public  boolean AddUser(AddUser adduser,DatabaseConnection connection);
	
}
