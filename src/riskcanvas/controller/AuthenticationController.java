package riskcanvas.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import bahriskcanvas.Success;
import riskcanvas.model.*;
import riskcanvas.service.AuthenticatonService;

@RestController
public class AuthenticationController 
{
	@Autowired
	AuthenticatonService authenticationService;
	
	@RequestMapping(value="change-password/{userName}",method=RequestMethod.POST)
	public Success changePassword(@RequestHeader(value="alfTicket",required=false)String alfTicket,@RequestBody Password password,HttpServletRequest request)
	{
		boolean result=authenticationService.changePassword(alfTicket, password, request);
		return new Success(result);
	}

}
