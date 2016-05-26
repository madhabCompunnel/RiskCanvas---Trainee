/**
 * @author 13089
 *@date 20/5/2016
 * @time 01:07 PM
 * RestServiceController for User Service
 */
package riskcanvas.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import utils.VerifyBoolean;
import bahriskcanvas.Success;
import riskcanvas.dao.DatabaseConnection;
import riskcanvas.dao.GetConfig;
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
	VerifyBoolean verify=new VerifyBoolean();
		/*****************************************************************************************************************************************************/	
		/*****************************************************       FOR CREATING NEW USER      **************************************************************/
		/*****************************************************************************************************************************************************/
		/**
		 * @param adduser
		 * @param req
		 * @param ticket
		 * @return success(true/false)
		 */
	     //Method for creating new user and mapping the incoming JSON request to AddUser class
		@RequestMapping(value="/create",method=RequestMethod.POST,consumes="application/json")
		public Success NewUser(@RequestBody AddUser adduser,HttpServletRequest req,@RequestHeader(value="alfTicket",required=false) String ticket) 
		{	
			boolean result=false;		
			adduser.setAlf_ticket(ticket);
		    verify.isboolean(adduser);//verifying if isActive and value fields carry boolean data or not
			conndata=GetConfig.getConnection(req);//Bean for setting database configurations
			result=userservice.add(adduser,conndata);
	        return new Success(result);
		}		
		
		/*****************************************************************************************************************************************************/	
		/*******************************************************       FOR EDITING USER      *****************************************************************/
		/*****************************************************************************************************************************************************/
		/**
		 * @param adduser
		 * @param req
		 * @param ticket
		 * @return success(true/false)
		 */
		//Method for editing user and mapping the incoming JSON request to AddUser class
		@RequestMapping(value="/edit/{userName}",method=RequestMethod.PUT,consumes="application/json")
		public Success EditUser(@RequestBody AddUser adduser,@PathVariable String userName,HttpServletRequest req,@RequestHeader(value="alfTicket",required=false) String ticket) 
		{	boolean result=false;	
			adduser.setAlf_ticket(ticket);
		    verify.isboolean(adduser);//verifying if isActive and value fields carry boolean data or not
			conndata=GetConfig.getConnection(req);//Bean for setting database configurations
			result=userservice.edit(adduser,conndata,userName);
	        return new Success(result);
		}		
}

