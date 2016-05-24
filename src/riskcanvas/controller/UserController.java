/**
 * @author 13089
 *@date 20/5/2016
 * @time 01:07 PM
 */
package riskcanvas.controller;

import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import bahriskcanvas.Success;
import riskcanvas.dao.DatabaseConnection;
import riskcanvas.dao.GetConfig;
import riskcanvas.exception.CustomException;
import riskcanvas.model.AddUser;
import riskcanvas.service.UserService;

@RestController 
public class UserController {
	private UserService userservice;
	DatabaseConnection conndata=new DatabaseConnection();
/*****************************************************************************************************************************************************/	
/*****************************************************       FOR CREATING NEW USER      **************************************************************/
/*****************************************************************************************************************************************************/
		/**
		 * @param roleinput
		 * Type CreateRoelInput
		 * @param req
		 * Type HttpSErvletRequest
		 * @return success(true/false)
		 */
		@RequestMapping(value="/user/create",method=RequestMethod.POST,consumes="application/json")//Mapping the incoming JSON request to CreateRoleInput class
		public Success NewUser(@RequestBody AddUser adduser,HttpServletRequest req,@RequestHeader(value="alfTicket") String ticket) throws CustomException, SQLException
		{	
			
			adduser.setAlf_ticket(ticket);//setting createdBy property in CreateRoleInput class
			boolean result=false;
			
			/**********************Setting context for retrieving the configuration file**********************/
			conndata=GetConfig.getConnection(req);//Bean for setting database configurations
			result=userservice.add(adduser,conndata);
	        return new Success(result);
		}
}

