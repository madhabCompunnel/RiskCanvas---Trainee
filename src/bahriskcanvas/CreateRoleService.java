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
import bahriskcanvas.CreateRoleOutput;
import bahriskcanvas.customException;
import bahriskcanvas.getalf_ticket;

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
	 * @param createinput
	 * @param size
	 * @return boolean
	 * @throws SQLException
	 */
	
/*****************************************************************************************************************************************************/	
/*****************************************************       FOR CREATING NEW ROLE      **************************************************************/
/*****************************************************************************************************************************************************/
	public boolean insert(CreateRoleInput createinput,int size) throws SQLException
	{
		Boolean success;
		CheckRoleName checkrolename=new CheckRoleName();//method for checking if roleName already exists or if roleId(if already exists) is legitimate
		checkrolename.CheckAlreadyExist(null,createinput.getRoleName(),datasource);
	    /**
	     * Three tables are managed for various field of input
	     */
		String sql1 = "INSERT INTO tbl_createrole (roleid,roleName,isActive,roleType,defaultScreen,createdon,createdby) VALUES (?, ?, ?, ?, ? , ?, ?)";
		String sql2 = "INSERT INTO tbl_menulist (roleid,id,description,value,createdon,createdby) VALUES (?, ?, ? , ?, ?, ?)";
		String sql3 = "INSERT INTO tbl_permissions (roleid,menuid,id,description,value,createdon,createdby) VALUES (?, ?, ?, ?, ?, ?, ?)";
		getuniqueid uniqueid=new getuniqueid();
		String id=uniqueid.uniqueid();//getting unique ten digit id
		String roleid="ROLE_"+createinput.getRoleName()+"_"+id;//unique roleId for each new user
		getalf_ticket getalfticket=new getalf_ticket();//getting current user alf_ticket
		String user=getalfticket.getticket(createinput.getAlf_ticket(), datasource, conn);
		CreateRoleOutput output=new CreateRoleOutput();//object for setting and getting output
		/*
		 * creating a timeStamp
		 */
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

		try 
		{
			conn = datasource.getConnection();//creating connection to database
			PreparedStatement ps1 = conn.prepareStatement(sql1);
			ps1.setString(1, roleid);
			ps1.setString(2, createinput.getRoleName());
			ps1.setString(3, createinput.getIsActive());
			ps1.setString(4, createinput.getRoleType());
			ps1.setString(5, createinput.getDefaultScreen());
			ps1.setTimestamp(6, currentTimestamp);
			ps1.setString(7,user);
			ps1.executeUpdate();//executing query

			
		    ps1 = conn.prepareStatement(sql2);
		    ps1.setString(1, roleid);
		    ps1.setString(2, createinput.getMenulist().get(0).getId());
		    ps1.setString(3, createinput.getMenulist().get(0).getDescription());
		    ps1.setString(4, createinput.getMenulist().get(0).getValue());
		    ps1.setTimestamp(5, currentTimestamp);
		    ps1.setString(6,user);
		    ps1.executeUpdate();//executing query
		    
		    for(int i=0;i<size;i++)//for storing multiple permissions related to same menuList in database
		    {
		    	ps1 = conn.prepareStatement(sql3);
		    	ps1.setString(1, roleid);
		    	ps1.setString(2, createinput.getMenulist().get(0).getId());
		    	ps1.setString(3, createinput.getMenulist().get(0).getPermissions().get(i).getId());
		    	ps1.setString(4, createinput.getMenulist().get(0).getPermissions().get(i).getDescription());
		    	ps1.setString(5, createinput.getMenulist().get(0).getPermissions().get(i).getValue());
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
/*****************************************************************************************************************************************************/	
/*******************************************************       FOR EDITING ROLE      *****************************************************************/
/*****************************************************************************************************************************************************/
	public boolean update(CreateRoleInput roleinput,int size) throws SQLException
	{

		Boolean success;//variable for sending true/false as output
		CheckRoleName checkrolename=new CheckRoleName();//method for checking if roleName already exists or if roleId is legitimate
		checkrolename.CheckAlreadyExist(roleinput.getRoleId(),roleinput.getRoleName(),datasource);
		/*************replacing roleName substring in roleId to match the new roleName***************/
		String roleid=roleinput.getRoleId();
		int startindex = roleid.indexOf("_");//counting start index for RoleId's substring carrying roleName
		int endindex =roleid.indexOf("_", startindex + 1);//counting end index for RoleId's substring carrying roleName
		StringBuilder newroleid=new StringBuilder();
		newroleid.append(roleid);
		newroleid.replace(startindex+1,endindex,roleinput.getRoleName());

	    /**
	     * Three tables are managed for various field of input
	     */
		String sql1 = "UPDATE tbl_createrole SET roleName=?,isActive=?,defaultScreen=?,updatedon=?,updatedby=? WHERE roleid=?";
		String sql2 = "UPDATE tbl_menulist SET description=?,value=?,updatedon=?,updatedby=? WHERE roleid=?";
		String sql3 = "UPDATE tbl_permissions SET description=?,value=?,updatedon=?,updatedby=? WHERE roleid=?";
		String sql4=  "UPDATE tbl_createrole SET roleId=? WHERE roleName=?";
		
		getalf_ticket getalfticket=new getalf_ticket();//getting current user alf_ticket
		String user=getalfticket.getticket(roleinput.getAlf_ticket(), datasource, conn);
		CreateRoleOutput output=new CreateRoleOutput();//role output object for setting and getting output
		/*
		 * creating a time stamp
		 */
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

		try 
		{
			conn = datasource.getConnection();//creating connection to database
			PreparedStatement ps1 = conn.prepareStatement(sql1);
			ps1.setString(1, roleinput.getRoleName());
			ps1.setString(2, roleinput.getIsActive());
			ps1.setString(3, roleinput.getDefaultScreen());
			ps1.setTimestamp(4, currentTimestamp);
			ps1.setString(5,user);
			ps1.setString(6, roleinput.getRoleId());
			ps1.executeUpdate();//executing query
		    
		    ps1 = conn.prepareStatement(sql2);
		    ps1.setString(1, roleinput.getMenulist().get(0).getDescription());
		    ps1.setString(2, roleinput.getMenulist().get(0).getValue());
		    ps1.setTimestamp(3, currentTimestamp);
		    ps1.setString(4,user);
		    ps1.setString(5, roleinput.getRoleId());
		    ps1.executeUpdate();//executing query
		    
		    for(int i=0;i<size;i++)//for storing multiple permissions related to same menuList in database
		    {
		    	ps1 = conn.prepareStatement(sql3);
		    	ps1.setString(1, roleinput.getMenulist().get(0).getPermissions().get(i).getDescription());
		    	ps1.setString(2, roleinput.getMenulist().get(0).getPermissions().get(i).getValue());
		    	ps1.setTimestamp(3, currentTimestamp);
		    	ps1.setString(4,user);
		    	ps1.setString(5, roleinput.getRoleId());
		    	ps1.executeUpdate();//executing query
		    }
		    
		    roleinput.setRoleId(newroleid.toString());//setting new roleId
		    ps1 = conn.prepareStatement(sql4);//for renaming roleId as per changed roleName
		    ps1.setString(1, roleinput.getRoleId() );
		    ps1.setString(2, roleinput.getRoleName());
		    ps1.executeUpdate();//executing query
		    ps1.close();
		    
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

}
