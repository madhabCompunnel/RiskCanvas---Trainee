package riskcanvas.controller;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import riskcanvas.model.EditGroup;
import bahriskcanvas.Success;
import riskcanvas.service.GroupService;
import riskcanvas.exception.UserException;

@RestController
public class UpdatedGroupController 
{
	@Autowired
	GroupService groupService;
	/**
	 * 
	 * @param editGroup
	 * @param request
	 * @return
	 * @throws UserException
	 * @throws riskcanvas.exception.UserException 
	 * 
	 */
	@RequestMapping(value="group/edit",method=RequestMethod.POST,consumes="application/json",produces="application/json")
	public Success editGroup(@RequestHeader(value="alfTicket",required=false) String alfTicket,@RequestBody EditGroup editGroup,HttpServletRequest request)
	{
		boolean result=groupService.editGroup(alfTicket,editGroup,request);
		return new Success(result);
	}
}
