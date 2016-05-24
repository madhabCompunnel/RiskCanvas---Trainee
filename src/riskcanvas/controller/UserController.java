/**
 * @author 13089
 *@date 20/5/2016
 * @time 01:07 PM
 * RestServiceController for User Service
 */
package riskcanvas.controller;

import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import utils.VerifyBoolean;
import bahriskcanvas.Success;
import riskcanvas.dao.DatabaseConnection;
import riskcanvas.dao.GetConfig;
import riskcanvas.exception.CustomException;
import riskcanvas.model.AddUser;
import riskcanvas.service.UserService;

//Controller for User Service
@RestController 
@RequestMapping(value="/user")//Mapping url path
public class UserController {
	
	//AutoWiring (by type)Business layer bean for dependency injection and avoiding NPE
	@Autowired
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
	//Mapping url path, headers in request and the incoming JSON request to AddUser class
		@RequestMapping(value="/create",method=RequestMethod.POST,consumes="application/json")
		public Success NewUser(@RequestBody AddUser adduser,HttpServletRequest req,@RequestHeader(value="alfTicket") String ticket) throws CustomException,SQLException
		{				
			adduser.setAlf_ticket(ticket);
			boolean result=false;
			VerifyBoolean verify=new VerifyBoolean();//verifying if isActive and value fields carry boolean data or not
		    verify.isboolean(adduser);
		
			/**********************Setting context for retrieving the configuration file**********************/
			conndata=GetConfig.getConnection(req);//Bean for setting database configurations
			result=userservice.add(adduser,conndata);
	        return new Success(result);
		}		
}

