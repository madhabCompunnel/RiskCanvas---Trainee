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
	
	public static DatabaseConnection getConnection(HttpServletRequest request)
	{
		WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
		DatabaseConnection databaseconnection= (DatabaseConnection) applicationContext.getBean("DBUtility1");
        return databaseconnection;
		
	}

}
