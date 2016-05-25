package riskcanvas.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import riskcanvas.model.*;

@RestController
public class AuthorizationController 
{
	@RequestMapping(value="change-password/{userName}",method=RequestMethod.POST)
	public Success changePassword(@RequestBody Password password)
	{
		
	}

}
