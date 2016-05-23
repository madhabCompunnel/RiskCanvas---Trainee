package bahriskcanvas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

import javax.sql.DataSource;

public class UserService {
	
	private DataSource datasource;//Spring's own class for setting database related configuration in Beans.xml file

	Connection conn =null;//Connection string
	/**
	 * @param createinput
	 * @param size
	 * @return boolean
	 * @throws SQLException
	 */
	
/*****************************************************************************************************************************************************/	
/*****************************************************       FOR CREATING NEW USER      **************************************************************/
/*****************************************************************************************************************************************************/
	public boolean add(AddUser adduser,DatabaseConnection conndata) throws SQLException
	{
		Boolean success;
		datasource=conndata.getDatasource();
		getalf_ticket getalfticket=new getalf_ticket();//getting current user alf_ticket
		String user=getalfticket.getticket(adduser.getAlf_ticket(), datasource, conn);
		//Check if username or email already exists------------TODO
		
	    /**
	     * Five tables are managed for various field of input
	     */
		String insertUser = "INSERT INTO tbl_user (username,firstname,lastname,phonenumber,email,roleId,isActive,defaultScreen,createdon,createdby) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,)";
		String userMenulist = "INSERT INTO tbl_usermenulist (userid,id,description,value,createdon,createdby) VALUES (?, ?, ? , ?, ?, ?)";
		String userPermission = "INSERT INTO tbl_userpermission (userid,menuid,id,description,value,createdon,createdby) VALUES (?, ?, ?, ?, ?, ?, ?)";
		String userGroup = "INSERT INTO tbl_user_groups(group_id,user_id) VALUES(?, ?)";
		String userRole = "INSERT INTO user_role(roleId,user_id) VALUES(?, ?)";
		String getCurrentUserid="SELECT user_id from tbl_user WHERE email='";
		
		/*
		 * creating a timeStamp
		 */
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

		try 
		{
			conn = datasource.getConnection();//creating connection to database
			PreparedStatement ps1 = conn.prepareStatement(insertUser);
			ps1.setString(1, adduser.getUserName());
			ps1.setString(2, adduser.getFirstname());
			ps1.setString(3, adduser.getLastname());
			ps1.setString(4, adduser.getPhonenumber());
			ps1.setString(5, adduser.getEmail());
			ps1.setString(6, adduser.getRoleId());
			ps1.setString(7, adduser.getIsActive());
			ps1.setString(8, adduser.getDefaultScreen());
			ps1.setString(9, currentTimestamp.toString());
			ps1.setString(10, user);

			
			
        success=true;
        return success;
		} 
		catch (SQLException e) 
		{
		    throw new customException(e.getErrorCode(),e.getMessage());//throwing custom exception
		} 
		finally 
		{
			if (conn != null) 
			{
				try
				{
					conn.close();
				} 
				catch (SQLException e) 
				{
					throw new customException(e.getErrorCode(),e.getMessage());//throwing custom exception
				}
			}
		}
		
	}

}
