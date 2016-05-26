/**
 * @author 13089
 * @date 26/5/2016
 * RestServiceController for Forgot Password and Reset Password Service
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

import bahriskcanvas.Success;

import riskcanvas.dao.DatabaseConnection;
import riskcanvas.dao.GetConfig;
import riskcanvas.model.*;
import riskcanvas.service.AuthenticationService;

//Controller for Forgot Password and Reset Password Service
@RestController
public class AuthenticationController 
{
	//AutoWiring (by type)Business layer bean for dependency injection and avoiding NPE
	@Autowired
	AuthenticationService authenticationService;
	DatabaseConnection conndata=new DatabaseConnection();
	
	/**
	 * @param userName
	 * @param alfTicket
	 * @param password
	 * @param request
	 * @return success(true/false)
	 */
	//Method for resetting and changing password
	@RequestMapping(value="change-password/{userName}",method=RequestMethod.POST)
	public Success changePassword(@PathVariable String userName,@RequestHeader(value="alfTicket",required=false)String alfTicket,@RequestBody Password password,HttpServletRequest request)
	{
		conndata=GetConfig.getConnection(request);//Bean for setting database configurations
		boolean result=authenticationService.changePassword(userName,alfTicket, password, conndata);
		return new Success(result);
	}
}
