package bahriskcanvas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Class returns the User_id
 */
public class UserId 
{
	private String user_id;
	public String getUserId(Connection con,String alfTicket)throws SQLException,NullPointerException,UserException
	{
		PreparedStatement getUserStatement=con.prepareStatement("select user_id from tbl_user_ticket where alf_ticket=?");
		getUserStatement.setString(1, alfTicket);
		ResultSet getUser=getUserStatement.executeQuery();
		while(getUser.next())
		{
			user_id=getUser.getString(1);
		}				
		return user_id;
	}
	

}
