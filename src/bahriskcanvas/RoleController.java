/**
 * 
 * @author 13089
 * @date 13/5/2016
 * @time 10:37 AM
 *
 */
//Role Service Controller
package bahriskcanvas;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import bahriskcanvas.CreateRoleInput;
import bahriskcanvas.CreateRoleService;
import bahriskcanvas.CreateRoleOutput;
import bahriskcanvas.GetConfig;
import bahriskcanvas.customException;

@RestController 
public class RoleController 
{
	CreateRoleService createroleservice=new CreateRoleService();
	DatabaseConnection conndata=new DatabaseConnection();
/*****************************************************************************************************************************************************/	
/*****************************************************       FOR CREATING NEW ROLE      **************************************************************/
/*****************************************************************************************************************************************************/
	/**
	 * @param roleinput
	 * Type CreateRoelInput
	 * @param req
	 * Type HttpSErvletRequest
	 * @return success(true/false)
	 */
	@RequestMapping(value="/role/create",method=RequestMethod.POST,consumes="application/json")//Mapping the incoming JSON request to CreateRoleInput class
	public CreateRoleOutput RegisterRole(@RequestBody CreateRoleInput roleinput,HttpServletRequest req,@RequestHeader(value="alfTicket") String ticket) throws customException
	{	
		roleinput.setAlf_ticket(ticket);//setting createdBy property in CreateRoleInput class
		CreateRoleOutput success=new CreateRoleOutput();
		int menusize=roleinput.getMenulist().size();
		VerifyBoolean verify=new VerifyBoolean();//verifying if isActive and value fields carry boolean data or not
	    verify.isboolean(roleinput);
		
		/**********************Setting context for retrieving the configuration file**********************/
		conndata=GetConfig.getConnection(req);//Bean for setting database configurations
		try 
		{
			boolean output=createroleservice.insert(roleinput,menusize,conndata);
			success.setSuccess(output);
		} 
		catch (SQLException e) 
		{
			success.setSuccess(false);
			throw new customException(e.getErrorCode(), e.getMessage());
		}
        return success;
	}

/*****************************************************************************************************************************************************/	
/*******************************************************       FOR EDITING ROLE      *****************************************************************/
/*****************************************************************************************************************************************************/
	/**
	 * @param editinput
	 * Type CreateRoelInput
	 * @param req
	 * Type HttpSErvletRequest
	 * @return success(true/false)
	 */
	@RequestMapping(value="/role/edit",method=RequestMethod.POST,consumes="application/json")//Mapping the incoming JSON request to CreateRoleInput class
	public CreateRoleOutput EditRole(@RequestBody CreateRoleInput editinput,HttpServletRequest req,@RequestHeader(value="alfTicket") String ticket)
	{	
		editinput.setAlf_ticket(ticket);//setting createdBy property in CreateRoleInput class
		CreateRoleOutput success=new CreateRoleOutput();
		int size=editinput.getMenulist().get(0).getPermissions().size();//to get size of the menuList array in input JSON
		
		VerifyBoolean verify=new VerifyBoolean();//verifying if isActive and value fields carry boolean data or not
	    verify.isboolean(editinput);
		
		/**********************Setting context for retrieving the configuration file**********************/
		conndata=GetConfig.getConnection(req);//Bean for setting database configurations
		try 
		{
			boolean output=createroleservice.update(editinput,size,conndata);
			success.setSuccess(output);
		} 
		catch (SQLException e) 
		{
			success.setSuccess(false);
			throw new customException(e.getErrorCode(), e.getMessage());
		}
        return success;
	}

/*****************************************************************************************************************************************************/	
/*******************************************************       FOR LISTING ROLE      *****************************************************************/
/*****************************************************************************************************************************************************/
	/**
	 * @param editinput
	 * Type CreateRoelInput
	 * @param req
	 * Type HttpSErvletRequest
	 * @return success(true/false)
	 * @throws JSONException 
	 */
	@RequestMapping(value="/role/list",method=RequestMethod.POST,consumes="application/json")//Mapping the incoming JSON request to CreateRoleInput class
	public Roles Register(@RequestBody String excludeInactive,HttpServletRequest req) throws customException, Exception
	{	
		
		DatabaseConnection conndata=new DatabaseConnection();
		conndata=GetConfig.getConnection(req);
		System.out.println("connData ="+conndata.toString());
		Roles rolelist=new Roles();
		JSONObject jsonobject=new JSONObject(excludeInactive);
		excludeInactive=jsonobject.getString("excludeInactive");
		if(excludeInactive.equals("true")||excludeInactive.equals("false"))
		{
		/**********************Setting context for retrieving the configuration file**********************/
			//createroleservice = GetConfig.getConfigRole(req);//Bean for setting database configurations
			try 
			{
				System.out.println("reached here");
				rolelist=createroleservice.list(excludeInactive,conndata);
				System.out.println("reached here 2");
			} 
			catch (SQLException e) 
			{
				throw new customException(e.getErrorCode(), e.getMessage());
			}
			return rolelist;
		}
		else
			throw new customException(400,"Only boolean values allowed in field 'ExcludeInactive'");
	}
	
	/*****************************************************************************************************************************************************/	
	/***************************************************       FOR HANDLING CUSTOM EXCEPTION      ********************************************************/
	/*****************************************************************************************************************************************************/
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