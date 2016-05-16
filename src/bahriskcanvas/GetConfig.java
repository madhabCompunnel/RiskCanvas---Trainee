package bahriskcanvas;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class GetConfig 
{
	public static ConnectionClass getConfig(HttpServletRequest request)
	{
		WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        ConnectionClass connectionClass = (ConnectionClass) applicationContext.getBean("DAO");
        return connectionClass;
		
	}
	
	public static CreateRoleService getConfig1(HttpServletRequest request)
	{
		WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
		CreateRoleService createroleservice= (CreateRoleService) applicationContext.getBean("DBUtility");
        return createroleservice;
		
	}

}
