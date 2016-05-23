package riskcanvas.controller;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import bahriskcanvas.ConnectionClass;
import bahriskcanvas.EditGroup;
import bahriskcanvas.GetConfig;
import bahriskcanvas.Success;
import bahriskcanvas.UserException;

public class GroupController 
{
	/**
	 * 
	 * @param editGroup
	 * @param request
	 * @return
	 * @throws UserException
	 * 
	 */
	@RequestMapping(value="group/edit",method=RequestMethod.POST,consumes="application/json",produces="application/json")
	public Success editGroup(@RequestHeader(value="alfTicket",required=false) String alfTicket,@RequestBody EditGroup editGroup,HttpServletRequest request) throws UserException
	{
		if(alfTicket==null)
		{
			throw new UserException("Header does not contains ticket");
		}
		if((editGroup.getGroupId()==null||editGroup.getGroupId().isEmpty())&& editGroup.getGroupName()==null||editGroup.getGroupName().isEmpty())
		{
			throw new UserException("check for group_Id and groupName,fields cannot be left empty");
		}
		ConnectionClass connectionClass =GetConfig.getConfig(request);
        boolean result=false;
			try 
			{
				result = connectionClass.getResult(editGroup,alfTicket);
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
	
	
}
