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
import utils.CheckValues;

/**
 * 
 * @author 13083
 *	Class deals with the Group Operations
 */
@RestController
public class UpdatedGroupController 
{
	@Autowired
	private GroupService groupService;
	/**
	 * 
	 * @param editGroup
	 * @param request
	 * @return
	 * @throws UserException
	 * @throws riskcanvas.exception.UserException 
	 * Method returns Success Object if the request is processed successfully for Edit Group
	 */
	@RequestMapping(value="group/edit",method=RequestMethod.PUT,consumes="application/json",produces="application/json")
	public Success editGroup(@RequestHeader(value="alfTicket",required=false) String alfTicket,@RequestBody EditGroup editGroup,HttpServletRequest request)
	{	
		new CheckValues().checkNull(alfTicket,"Alf_Ticket");
		boolean result=groupService.editGroup(alfTicket,editGroup,request);
		return new Success(result);
	}
}
