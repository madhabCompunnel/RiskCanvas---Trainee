package bahriskcanvas;

import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bahriskcanvas.ConnectionClass;
/*
 * Class deals with the Group Operations
 */
@RestController
public class GroupController 
{
	/**
	 * 
	 * @param createGroup
	 * @param alfTicket
	 * @param request
	 * @return
	 * @throws UserException
	 * Method create is responsible for creating a group and returns success object if no exception occurs
	 */
	@RequestMapping(value="group/create",method=RequestMethod.POST)
	public Success createGroup(@RequestBody CreateGroup createGroup,@RequestHeader(value="alfTicket",required=false) String alfTicket,HttpServletRequest request) throws UserException
	{
		//result will hold true if create group is successful
		boolean result=false;
		//alfTicket we receive from the request header to check if User is logged In
		if(alfTicket==null)
		{
			throw new UserException("Header does not contains ticket");
		}
		
		if(createGroup.getGroupName().isEmpty()||createGroup.getGroupName()==null)
		{
			throw new UserException("Add Group Name");
		}
		else
		{
			 ConnectionClass connectionClass =GetConfig.getConfig(request);
        
			try
				{
				result = connectionClass.getResult(createGroup,alfTicket);
				}
			catch (UserException e) 
				{
				throw new UserException(e.getErrorMessage());
				}
			catch (SQLException e) 
				{	
				throw new UserException(e.getMessage());
				}
			catch(NullPointerException e)
				{
				throw new UserException(e.getMessage());
				}
			catch(Exception e)
			{
			throw new UserException(e.getMessage());
			}
		}
		return new Success(result);
	}
	
	
	
	/**
	 * 
	 * @param group_id
	 * @param request
	 * @return
	 * @throws UserException
	 */
	@RequestMapping(value="group/delete")
	public Success deleteGroup(@RequestHeader(value="alfTicket",required=false) String alfTicket,@RequestParam("groupId") String group_id,HttpServletRequest request) throws Exception
	{
		if(alfTicket==null)
		{
			throw new UserException("Header does not contains ticket");
		}
		if(group_id==null||group_id.isEmpty())
		{
		throw new UserException("groupId cannot be left empty");	
		}
		else
		{
		ConnectionClass connectionClass =GetConfig.getConfig(request);
        boolean result=false;
			try {
				result = connectionClass.getResult(group_id,alfTicket);
				}
			catch (NullPointerException e)				
				{
				throw new UserException(e.getMessage());
				}
			catch (SQLException e) 
				{
				throw new UserException(e.getMessage());
				}
			catch(Exception e)
				{
				throw new UserException(e.getMessage());
				}
		return new Success(result);
		}
	}
	public Success moveGroup(@RequestBody MoveGroup moveGroup,HttpServletRequest request)
	{
		ConnectionClass connectionClass =GetConfig.getConfig(request);
        boolean result=connectionClass.getResult(moveGroup);
		return new Success(result);
	}
	
	/**
	 * @param alfTicket
	 * @param request
	 * @return
	 * @throws UserException
	 * @throws SQLException
	 * @throws NullPointerException
	 * Method returns all Groups
	 */
	@RequestMapping(value="group/all")
	public Groups listAll(@RequestHeader(value="alfTicket",required=false) String alfTicket, HttpServletRequest request) throws UserException,SQLException,NullPointerException
	{
		Groups groups=null;
		if(alfTicket==null)
		{
			throw new UserException("Header does not contains ticket");
		}
		 ConnectionClass connectionClass =GetConfig.getConfig(request);
		 try 
		 {
			groups=connectionClass.getGroupList(alfTicket);
		 }
		 catch (UserException e) 
		 {
			throw new UserException(e.getErrorMessage());
		 }
		 catch (SQLException e) 
		 {
			throw new UserException(e.getMessage());
		 }
		 catch (NullPointerException e) 
		 {
			throw new UserException(e.getMessage());
		 } 
		 catch (Exception e) 
		 {
			throw new UserException(e.getMessage());
		 }
		return groups;
	}
	
	@ExceptionHandler(UserException.class)
	public ErrorResponse exceptionHandler(Exception ex) 
	{
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(HttpStatus.BAD_REQUEST);
		error.setMessage(ex.getMessage());
		return error;
	}
}
