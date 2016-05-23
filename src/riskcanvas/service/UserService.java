package riskcanvas.service;

import riskcanvas.dao.DatabaseConnection;
import riskcanvas.model.AddUser;

public interface UserService {

	boolean add(AddUser adduser, DatabaseConnection conndata);
	

}
