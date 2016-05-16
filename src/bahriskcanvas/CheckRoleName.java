//Class for checking if rolename already exists
package bahriskcanvas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;


/**
 * @param roleName
 * @param datasource
 * @throws SQLException
 * Method for checking if roleName already exists in database
 */
public class CheckRoleName {
	public void CheckAlreadyExist(String roleName,DataSource datasource) throws SQLException
	{
		Connection conn=null;
		String sql1 = "SELECT * from tbl_createrole WHERE roleName=?";
		conn = datasource.getConnection();
		PreparedStatement ps4 = conn.prepareStatement(sql1);
		ps4.setString(1, roleName);
		ResultSet rs = ps4.executeQuery();//executing query
		rs.last();
		int rownum=rs.getRow();
		if(rownum>0)
		{
			while(rs.next())
			{
				System.out.println("In rolename while");
				throw new customException(505,"Duplicate entry:Role "+"'"+roleName+"'"+"already exists");
			}
		}
		ps4.close();	
	}	

}
