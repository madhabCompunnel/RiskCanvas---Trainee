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
	private int user_id=0;
	/**
	 * 
	 * @param con
	 * @param alfTicket
	 * @return
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws UserException
	 * Method returns user id
	 */
	public int getUserId(Connection con,String alfTicket)throws SQLException,NullPointerException,UserException
	{
		PreparedStatement getUserStatement=con.prepareStatement("select user_id from tbl_user_ticket where alf_ticket=?");
		getUserStatement.setString(1, alfTicket);
		ResultSet getUser=getUserStatement.executeQuery();
		while(getUser.next())
		{
			user_id=getUser.getInt(1);
		}				
		return user_id;
		}
}
