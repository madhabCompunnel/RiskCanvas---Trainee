package bahriskcanvas;

import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bahriskcanvas.ConnectionClass;

@RestController
public class GroupController 
{
	@RequestMapping(value="group/create",method=RequestMethod.POST)
	public Success createGroup(@RequestBody CreateGroup createGroup,HttpServletRequest request) throws UserException
	{	
		if(createGroup.getGroupName().isEmpty()||createGroup.getGroupName()==null)
		{
			throw new UserException("Add Group Name");
		}
		else
		{
			 ConnectionClass connectionClass =GetConfig.getConfig(request);
        boolean result=false;
			try
				{
				result = connectionClass.getResult(createGroup);
				return new Success(result);
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
		}
	}
	@RequestMapping(value="group/edit",method=RequestMethod.POST,consumes="application/json",produces="application/json")
	public Success editGroup(@RequestBody EditGroup editGroup,HttpServletRequest request) throws UserException
	{
		 ConnectionClass connectionClass =GetConfig.getConfig(request);
        boolean result=false;
			try 
			{
				result = connectionClass.getResult(editGroup);
			} 
			catch (NullPointerException e) 
			{
				throw new UserException(e.getMessage());
			}
			catch (SQLException e) 
			{
				throw new UserException(e.getMessage());
			}
			catch(UserException e)
			{
				throw new UserException(e.getErrorMessage());
			}
		return new Success(result);
	}
	@RequestMapping(value="group/delete")
	public Success deleteGroup(@RequestParam("groupId") String group_id,HttpServletRequest request) throws UserException
	{
		if(group_id==null||group_id.isEmpty())
		{
		throw new UserException("groupId cannot be left empty");	
		}
		else
		{
		ConnectionClass connectionClass =GetConfig.getConfig(request);
        boolean result=false;
			try {
				result = connectionClass.getResult(group_id);
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
	
	
	@ExceptionHandler(UserException.class)
	public ErrorResponse exceptionHandler(Exception ex) 
	{
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(HttpStatus.BAD_REQUEST);
		error.setMessage(ex.getMessage());
		return error;
	}
}
