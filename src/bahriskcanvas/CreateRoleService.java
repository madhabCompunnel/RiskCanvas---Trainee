/**
 * @author 13089
 * @date 13/5/2016
 * @time 10:37 AM
 *
 */
//main service for controlling and manipulating database based on the the json input from user
//
package bahriskcanvas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import javax.sql.DataSource;

import bahriskcanvas.CheckRoleName;
import bahriskcanvas.CreateRoleInput;

public class CreateRoleService 
{
	private DataSource datasource;//Spring's own class for setting database related configuration in Beans.xml file
	/** 
	* @param Datasource
	* The Datasource
	*/
	public void setDatasource(DataSource datasource)
	{
		this.datasource = datasource;
	}
	Connection conn =null;//Connection string
	/**
	 * @param ob
	 * @param size
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean insert(CreateRoleInput ob,int size) throws SQLException
	{

		Boolean success;
		CheckRoleName checkrolename=new CheckRoleName();
		checkrolename.CheckAlreadyExist(ob.getRoleName(),datasource);
		//CheckAlreadyExist(ob.getRoleName(),datasource,conn);//method for checking if role already exists

	    /**
	     * Three tables are managed for various field of input
	     */
		String sql1 = "INSERT INTO tbl_createrole (roleid,roleName,isActive,roleType,defaultScreen,createdon,createdby) VALUES (?, ?, ?, ?, ? , ?, ?)";
		String sql2 = "INSERT INTO tbl_menulist (roleid,id,description,value,createdon,createdby) VALUES (?, ?, ? , ?, ?, ?)";
		String sql3 = "INSERT INTO tbl_permissions (roleid,menuid,id,description,value,createdon,createdby) VALUES (?, ?, ?, ?, ?, ?, ?)";
		getuniqueid uniqueid=new getuniqueid();
		String id=uniqueid.uniqueid();//getting unique roleId
		getalf_ticket getalfticket=new getalf_ticket();//getting current user alf_ticket
		String user=getalfticket.getticket(ob.getAlf_ticket(), datasource, conn);
		String roleid="ROLE_"+ob.getRoleName()+"_"+id;
		CreateRoleOutput output=new CreateRoleOutput();//object for setting and getting output
		/*
		 * creating a timestamp
		 */
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

		try 
		{
			conn = datasource.getConnection();//creating connection to database
			PreparedStatement ps1 = conn.prepareStatement(sql1);
			ps1.setString(1, roleid);
			ps1.setString(2, ob.getRoleName());
			ps1.setBoolean(3, ob.getIsActive());
			ps1.setString(4, ob.getRoleType());
			ps1.setString(5, ob.getDefaultScreen());
			ps1.setTimestamp(6, currentTimestamp);
			ps1.setString(7,user);
			ps1.executeUpdate();//executing query
		    
		    ps1 = conn.prepareStatement(sql2);
		    ps1.setString(1, roleid);
		    ps1.setString(2, ob.getMenulist().get(0).getId());
		    ps1.setString(3, ob.getMenulist().get(0).getDescription());
		    ps1.setBoolean(4, ob.getMenulist().get(0).getValue());
		    ps1.setTimestamp(5, currentTimestamp);
		    ps1.setString(6,user);
		    ps1.executeUpdate();//executing query
		    
		    for(int i=0;i<size;i++)//for storing multiple permissions realted to same menulist in database
		    {
		    	ps1 = conn.prepareStatement(sql3);
		    	ps1.setString(1, roleid);
		    	ps1.setString(2, ob.getMenulist().get(0).getId());
		    	ps1.setString(3, ob.getMenulist().get(0).getPermissions().get(i).getId());
		    	ps1.setString(4, ob.getMenulist().get(0).getPermissions().get(i).getDescription());
		    	ps1.setBoolean(5, ob.getMenulist().get(0).getPermissions().get(i).getValue());
		    	ps1.setTimestamp(6, currentTimestamp);
		    	ps1.setString(7,user);
		    	ps1.executeUpdate();//executing query
		    	ps1.close();
		    }
		    output.setSuccess(true);
		    success=output.getSuccess();
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
	
	/**
	 * @param roleName
	 * @throws SQLException
	 * Method for checking if roleName already exists in database
	 */
	/*private void CheckAlreadyExist(String roleName) throws SQLException
	{
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
	}	*/
}
