package bahriskcanvas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserCount 
{
	private static int count=0;
	public static int getUserCount(Connection con,String group_id) throws SQLException
	{
		PreparedStatement getUserCount=con.prepareStatement("select count(user_id) from tbl_user_groups where group_id=?");
		getUserCount.setString(1, group_id);
		ResultSet rs=getUserCount.executeQuery();
		if(rs.next())
			count=rs.getInt(1);
		else
			count=0;
		return count;
	}

}
