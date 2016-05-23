/**
 * @author 13089
 *@date 20/5/2016
 * @time 01:07 PM
 */
package bahriskcanvas;

import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class UserController {
	UserService userservice=new UserService();
	DatabaseConnection conndata=new DatabaseConnection();
/*****************************************************************************************************************************************************/	
/*****************************************************       FOR CREATING NEW USER      **************************************************************/
/*****************************************************************************************************************************************************/
		/**
		 * @param roleinput
		 * Type CreateRoelInput
		 * @param req
		 * Type HttpSErvletRequest
		 * @return success(true/false)
		 */
		@RequestMapping(value="/user/create",method=RequestMethod.POST,consumes="application/json")//Mapping the incoming JSON request to CreateRoleInput class
		public Success NewUser(@RequestBody AddUser adduser,HttpServletRequest req,@RequestHeader(value="alfTicket") String ticket) throws customException
		{	
			adduser.setAlf_ticket(ticket);//setting createdBy property in CreateRoleInput class
			boolean result=false;
			
			/**********************Setting context for retrieving the configuration file**********************/
			conndata=GetConfig.getConnection(req);//Bean for setting database configurations
			try 
			{
				result=userservice.add(adduser,conndata);
			} 
			catch (SQLException e) 
			{
				throw new customException(e.getErrorCode(), e.getMessage());
			}
	        return new Success(result);
		}
		/**
		 * @param ex
		 * @return
		 * The exception
		 */
		@ExceptionHandler(customException.class)
		@ResponseBody
		public throwException handleCustomException(customException ex) 
		{
			throwException exception=new throwException();
			exception.setSuccess(false);
			exception.setErrcode(ex.getErrcode());
			exception.setErrmsg(ex.getErrmsg());
			return exception;
		}
	}

