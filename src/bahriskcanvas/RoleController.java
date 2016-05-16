/**
 * 
 * @author 13089
 * @date 13/5/2016
 * @time 10:37 AM
 *
 */
//Create Role Service Controller
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
import bahriskcanvas.CreateRoleInput;
import bahriskcanvas.CreateRoleService;

@RestController 
public class RoleController 
{
	CreateRoleService createroleservice=new CreateRoleService();
	/**
	 * @param roleinput
	 * Type CreateRoelInput
	 * @param req
	 * Type HttpSErvletRequest
	 * @return success(true/false)
	 */
	@RequestMapping(value="/role/create",method=RequestMethod.POST,consumes="application/json")//Mapping the incoming JSON request to CreateRoleInput class
	public CreateRoleOutput Register(@RequestBody CreateRoleInput roleinput,HttpServletRequest req,@RequestHeader(value="alfTicket") String ticket) throws customException
	{	
		roleinput.setAlf_ticket(ticket);//setting createdBy property in CreateRoleInput class
		CreateRoleOutput success=new CreateRoleOutput();
		int size=roleinput.getMenulist().get(0).getPermissions().size();//to get size of the menuList array in input JSON
		
		/**********************Setting context for retrieving the configuration file**********************/
		createroleservice = GetConfig.getConfig1(req);//Bean for setting database configurations
		try 
		{
			boolean output=createroleservice.insert(roleinput,size);
			success.setSuccess(output);
		} 
		catch (SQLException e) 
		{
			success.setSuccess(false);
			throw new customException(e.getErrorCode(), e.getMessage());
		}
        return success;
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
