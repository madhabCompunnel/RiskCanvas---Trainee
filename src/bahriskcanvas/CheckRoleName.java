//class for checking if rolename already exists
package bahriskcanvas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;


/**
 * @param roleName
 * @throws SQLException
 * Method for checking if roleName already exists in database
 */
public class CheckRoleName {

;
	public void CheckAlreadyExist(String roleId,String roleName,DataSource datasource) throws SQLException
	{
		
		Connection conn=datasource.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		/**********Checking if roleName already exists for creating and editing role*********************/
		String sql1 = "SELECT * from tbl_createrole WHERE roleName=?";
		ps= conn.prepareStatement(sql1);
		ps.setString(1, roleName);
		rs = ps.executeQuery();//executing query
		if(rs.next())
			{
				throw new customException(505,"Duplicate entry:Role "+"'"+roleName+"'"+"already exists");
			}
       
		
		/***************************Checking if roleId exists for editing role*************************/
		if(roleId!=null)
		{
			String sql2="SELECT * from tbl_createrole WHERE roleId=?";
			ps= conn.prepareStatement(sql2);
			ps.setString(1, roleId);
			rs = ps.executeQuery();//executing query
			if(!rs.next())
			{
				throw new customException(505,"No such roleId exists :"+roleId);
			}
			ps.close();	
	    }	
		
	}

}
