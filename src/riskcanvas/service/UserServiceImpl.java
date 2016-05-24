//UserService Implementation
package riskcanvas.service;

import riskcanvas.dao.DatabaseConnection;
import riskcanvas.dao.user.UserDao;
import riskcanvas.model.AddUser;

public class UserServiceImpl implements UserService {
private UserDao userdao;//reference to UserDao Interface for dependency injection

public void setUserdao(UserDao userdao) {
		this.userdao = userdao;
	}

@Override
public boolean add(AddUser adduser, DatabaseConnection conndata) {
		Boolean success=false;
		success=userdao.AddUser(adduser, conndata);//input to dataLayer(userDao implementation)and retrieving output result
		return success;
	}
}
