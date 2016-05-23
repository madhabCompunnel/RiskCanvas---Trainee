package bahriskcanvas;
/*
 * This is a controller class for Login
 */
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
/*
 * class is returning User Object in the form of JSON if the credentials holds true.
 */
public class LoginController 
{
	/**
	 * 
	 * @param credentials
	 * @param request
	 * @return
	 * @throws UserException
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public User login(@RequestBody Credentials credentials,HttpServletRequest request)throws UserException
	{
		/*
		 * check if user name is empty
		 */
		if(credentials.getUsername().isEmpty()||credentials.getUsername()==null)
		{
			throw new UserException("UserName field cannot be left empty");
		}
		/*
		 * check if password is empty
		 */
		if(credentials.getPassword().isEmpty()||credentials.getPassword()==null)
		{
			throw new UserException("Password field cannot be left empty");
		}
		/*
		 * This is connectionClass is used to get the DataSource Bean configuration.
		 */
        ConnectionClass connectionClass =GetConfig.getConfig(request);
        /*
         * User is the Object which is to be returned
         */
		User user=null;
		try 
		{
			/*
			 * get the user Object front the ConnectionClass
			 */
			user = connectionClass.getUser(credentials);
		} 
		catch (SQLException e) 
		{
			throw new UserException(e.getMessage());
		}
		catch(NullPointerException e)
		{
			throw new UserException("Sorry no record found");
		}
		catch(UserException e)
		{
			throw new UserException(e.getErrorMessage());
		}
		catch(Exception e)
		{
			throw new UserException(e.getMessage());
		}
		return user;
	}
	/**
	 * 
	 * @param ex
	 * @return
	 * Method returns Exception 
	 */
	@ExceptionHandler(UserException.class)
	public ErrorResponse exceptionHandler(Exception ex) {
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(HttpStatus.BAD_REQUEST);
		error.setMessage(ex.getMessage());
		return error;
	}
}
