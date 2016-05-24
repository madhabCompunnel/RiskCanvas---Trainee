//UserService Interface
package riskcanvas.service;

import riskcanvas.dao.DatabaseConnection;
import riskcanvas.model.AddUser;

public interface UserService {

	public boolean add(AddUser adduser, DatabaseConnection conndata);
		
}
