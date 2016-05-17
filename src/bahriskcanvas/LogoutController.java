
package bahriskcanvas;

import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
/*
 * Class remove the row from the tbl_user_ticket  
 */
public class LogoutController 
{
	/**
	 * 
	 * @param alfTicket
	 * @param request
	 * @return
	 * @throws UserException
	 * method responds to logout request
	 */
	@RequestMapping(value="/logout",method=RequestMethod.POST)
	public void logout(@RequestHeader("alfTicket") String alfTicket,HttpServletRequest request) throws UserException
	{
		if(alfTicket.isEmpty()||alfTicket==null)
		{
			throw new UserException("alfTicket is empty");
		}
		else
		{
		 ConnectionClass connectionClass =GetConfig.getConfig(request);
			try 
			{
				connectionClass.logout(alfTicket);
			} catch (UserException e) 
			{
				throw new UserException(e.getErrorMessage());
			}
			catch (SQLException e) 
			{
				throw new UserException(e.getMessage());
			}
			catch(Exception e)
			{
				throw new UserException(e.getMessage());
			}
		}
	}
	/**
	 * 
	 * @param ex
	 * @return
	 * Method Returns Exception
	 */
	@ExceptionHandler(UserException.class)
	public ErrorResponse exceptionHandler(Exception ex) 
	{
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(HttpStatus.BAD_REQUEST);
		error.setMessage(ex.getMessage());
		return error;
	}
}