//UserService Interface
package riskcanvas.service;

import riskcanvas.dao.DatabaseConnection;
import riskcanvas.model.AddUser;

public interface UserService {

	public boolean add(AddUser adduser, DatabaseConnection conndata);

	public boolean edit(AddUser adduser, DatabaseConnection conndata,String pathUsername);
		
}
