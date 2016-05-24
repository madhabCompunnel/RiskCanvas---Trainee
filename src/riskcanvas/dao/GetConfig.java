package riskcanvas.dao;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class GetConfig 
{
	public static riskcanvas.dao.DatabaseConnection getConnection(HttpServletRequest request)
	{
		WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
		DatabaseConnection databaseConnection=(DatabaseConnection) applicationContext.getBean("DBUtility");
        return databaseConnection;
		
	}

}
