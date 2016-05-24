//class for setting database related configurations
package riskcanvas.dao;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class GetConfig 
{
	/**
	 * @param request
	 * @return databaseConnection
	 */
	public static DatabaseConnection getConnection(HttpServletRequest request)
	{
		WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
		DatabaseConnection databaseConnection= (DatabaseConnection) applicationContext.getBean("DBUtility");
        return databaseConnection;
		
	}

}
