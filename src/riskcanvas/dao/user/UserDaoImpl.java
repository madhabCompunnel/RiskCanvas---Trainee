//data access layer class for dealing with inputs and outputs to and from database
package riskcanvas.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.sql.DataSource;

import utils.CheckTicket;
import utils.CheckValues;
import utils.RestrictChange;
import riskcanvas.dao.DatabaseConnection;
import riskcanvas.exception.CustomException;
import riskcanvas.model.AddUser;

public class UserDaoImpl implements UserDao{
	private DataSource datasource;//for getting database configurations from DatabaseConnection class
	Connection conn=null;//Connection String
	
	int userid;
	Boolean success=false;
	CheckValues value=new CheckValues();
	RestrictChange restrictChange=new RestrictChange();

/*****************************************************************************************************************************************************/	
/*****************************************************       FOR CREATING NEW USER      **************************************************************/
/*****************************************************************************************************************************************************/
	/**
	 * @param adduser
	 * @param connection 
	 * @return success(true/false)
	 */
	@Override
	public Boolean AddUser(AddUser adduser, DatabaseConnection connection)  {
		
		datasource=connection.getDatasource();
		int menusize=adduser.getMenulist().size();//to get size of menuList for any user for iteration in loop below
		
	    /**
	     * Five tables are managed for various field of input
	     */
		String insertUser = "INSERT INTO tbl_user(username,f_name,l_name,phonenumber,email,roleId,isActive,defaultScreen,createdon,createdby) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		String userMenulist = "INSERT INTO tbl_usermenulist(user_id,id,description,value,createdon,createdby) VALUES (?, ?, ? , ?, ?, ?)";
		String userPermission = "INSERT INTO tbl_userpermission(user_id,menuid,id,description,value,createdon,createdby) VALUES (?, ?, ?, ?, ?, ?, ?)";
		String userGroup = "INSERT INTO tbl_user_groups(group_id,user_id) VALUES(?, ?)";
		String userRole = "INSERT INTO user_role(roleId,user_id) VALUES(?, ?)";
		String getCurrentUserid="SELECT user_id from tbl_user WHERE email=?";
		
		/**
		 * creating a timeStamp
		 */
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
	
		try {
			value.checkNull(adduser.getAlf_ticket(),"Alf_ticket");
	        value.checkExist(adduser.getEmail(),"tbl_user","email",datasource);
	        value.checkExist(adduser.getUserName(),"tbl_user","username",datasource);
	        value.checkNotExist(adduser.getRoleId(),"tbl_createrole","roleId",datasource);
	        value.checkNotExist(adduser.getGroup().get(0).getGroupId(),"tbl_groups","group_id",datasource);
	        value.checkNotExist(adduser.getGroup().get(0).getGroup_name(),"tbl_groups","group_name",datasource);
	        value.checkMapping(adduser.getGroup().get(0).getGroupId(), adduser.getGroup().get(0).getGroup_name(), "tbl_groups","group_id","group_name", datasource);
			CheckTicket checkTicket=new CheckTicket();
			
			conn = datasource.getConnection();//creating connection to database
			conn.setAutoCommit(false);
			int user=checkTicket.getticket(adduser.getAlf_ticket(), conn);
/**********************************************************Adding data in tbl_user**************************************************/
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
			ps1.setInt(10, user);
			ps1.executeUpdate();//executing query
			ps1.close();

/************************************************Getting current userId from  tbl_user**********************************************/			
			ps1=conn.prepareStatement(getCurrentUserid);
			ps1.setString(1, adduser.getEmail());
			ResultSet rs=ps1.executeQuery();
			if(rs.next())
			{
				userid=rs.getInt(1);
			}
			rs.close();
			ps1.close();
			
/**********************************************************Adding data in tbl_userMenulist**************************************************/
			for(int i=0;i<menusize;i++)
			{
				PreparedStatement ps2 = conn.prepareStatement(userMenulist);
			    ps2.setInt(1, userid);
			    ps2.setString(2, adduser.getMenulist().get(i).getId());
			    ps2.setString(3, adduser.getMenulist().get(i).getDescription());
			    ps2.setString(4, adduser.getMenulist().get(i).getValue());
			    ps2.setTimestamp(5, currentTimestamp);
			    ps2.setInt(6,user);
			    ps2.executeUpdate();//executing query
			    ps2.close();
			}
			
/**********************************************************Adding data in tbl_userPermissions**************************************************/
			for(int i=0;i<menusize;i++)//for storing multiple permissions related to same menuList in database
		    {
		    	int size=adduser.getMenulist().get(i).getPermissions().size();
		    	for(int j=0;j<size;j++)
		    	{
		    		PreparedStatement ps3 = conn.prepareStatement(userPermission);
			    	ps3.setInt(1, userid);
			    	ps3.setString(2, adduser.getMenulist().get(i).getId());
			    	ps3.setString(3, adduser.getMenulist().get(i).getPermissions().get(j).getId());
			    	ps3.setString(4, adduser.getMenulist().get(i).getPermissions().get(j).getDescription());
			    	ps3.setString(5, adduser.getMenulist().get(i).getPermissions().get(j).getOverriddenValue());
			    	ps3.setTimestamp(6, currentTimestamp);
			    	ps3.setInt(7,user);
			    	ps3.executeUpdate();//executing query
			    	ps3.close();
		    	}
		    }
				
/**********************************************************Adding data in tbl_user_groups******************************************************/
			PreparedStatement ps4=conn.prepareStatement(userGroup);
			ps4.setString(1, adduser.getGroup().get(0).getGroupId());
			ps4.setInt(2, userid);
			ps4.executeUpdate();
			ps4.close();
	
/*************************************************************Adding data in user_role********************************************************/
			PreparedStatement ps5=conn.prepareStatement(userRole);
			ps5.setString(1, adduser.getRoleId());
			ps5.setInt(2, userid);
			ps5.executeUpdate();
			ps5.close();
			conn.commit();
			conn.close();
			
			success=true;
	        return success;
		} 
		catch (SQLException e) 
		{
			try {
				conn.rollback();
			} 
			catch (SQLException e1) {
				throw new CustomException(e1.getErrorCode(),e1.getMessage());//throwing custom exception for SQL Exception
			}
		    throw new CustomException(e.getErrorCode(),e.getMessage());//throwing custom exception for SQL Exception
		} 
		catch (CustomException e) 
		{
		    throw new CustomException(e.getErrcode(),e.getErrmsg());//throwing custom exception
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
					throw new CustomException(e.getErrorCode(),e.getMessage());//throwing custom exception
				}
			}
		}
		
	}

/*****************************************************************************************************************************************************/	
/******************************************************       FOR EDITING USER      ******************************************************************/
/*****************************************************************************************************************************************************/
	@Override
	public Boolean EditUser(riskcanvas.model.AddUser adduser, DatabaseConnection conndata,String pathUsername) {
		datasource=conndata.getDatasource();
		int menusize=adduser.getMenulist().size();//to get size of menuList for any user for iteration in loop below
		
	    /**
	     * Five tables are managed for various field of input
	     */
		
		String getCurrentUserid="SELECT user_id from tbl_user WHERE email=? and username=?";//throws exception if email and pathUsername does not map to same entity
		String editUser = "UPDATE tbl_user SET username=?,f_name=?,l_name=?,phonenumber=?,roleId=?,isActive=?,defaultScreen=?,updatedon=?,updatedby= ? WHERE email=?";
		String editMenulist = "INSERT into tbl_usermenulist(user_id,id,description,value,createdon,createdby) VALUES (?, ?, ? , ?, ?, ?) ON DUPLICATE KEY UPDATE description=?,value=?,updatedon=?,updatedby=?";
		String editPermission = "INSERT into tbl_userpermission(user_id,menuid,id,description,value,createdon,createdby) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE description=?,value=?,updatedon=?,updatedby=?";
		String edituserGroup = "UPDATE tbl_user_groups SET group_id=? WHERE user_id=?";
		String edituserRole = "UPDATE user_role SET roleId=? WHERE user_id=?";
	
		/**
		 * creating a timeStamp
		 */
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
		
		try{
			value.checkNull(adduser.getAlf_ticket(),"Alf_Ticket");
	        value.checkNotExist(adduser.getRoleId(),"tbl_createrole","roleId",datasource);
	        value.checkNotExist(adduser.getGroup().get(0).getGroupId(),"tbl_groups","group_id",datasource);
	        value.checkNotExist(adduser.getGroup().get(0).getGroup_name(),"tbl_groups","group_name",datasource);
	        value.checkMapping(adduser.getGroup().get(0).getGroupId(), adduser.getGroup().get(0).getGroup_name(), "tbl_groups", "group_id", "group_name", datasource);
	        CheckTicket checkTicket=new CheckTicket();
			
			conn = datasource.getConnection();//creating connection to database
			conn.setAutoCommit(false);//all changes to database will happen if every query executes or none will happen
			int user=checkTicket.getticket(adduser.getAlf_ticket(), conn);
/************************************************Getting current userId from  tbl_user**********************************************/			
			PreparedStatement ps1=conn.prepareStatement(getCurrentUserid);
			ps1.setString(1, adduser.getEmail());
			ps1.setString(2, pathUsername);
			ResultSet rs=ps1.executeQuery();
			if(rs.next())
			{
				userid=rs.getInt(1);
			}
			else
				throw new CustomException(405,"Email and username does not match");
			rs.close();
			ps1.close();
			
/**********************************************************Adding data in tbl_user**************************************************/
		    ps1 = conn.prepareStatement(editUser);
			ps1.setString(1, adduser.getUserName());
			ps1.setString(2, adduser.getFirstname());
			ps1.setString(3, adduser.getLastname());
			ps1.setString(4, adduser.getPhonenumber());
			ps1.setString(5, adduser.getRoleId());
			ps1.setString(6, adduser.getIsActive());
			ps1.setString(7, adduser.getDefaultScreen());
			ps1.setString(8, currentTimestamp.toString());
			ps1.setInt(9, user);
			ps1.setString(10, adduser.getEmail());
			ps1.executeUpdate();//executing query
			ps1.close();
			
/**********************************************************Adding data in tbl_userMenulist**************************************************/
			for(int i=0;i<menusize;i++)
			{
				ps1 = conn.prepareStatement(editMenulist);
				ps1.setInt(1, userid);
			    ps1.setString(2, adduser.getMenulist().get(i).getId());
			    ps1.setString(3, adduser.getMenulist().get(i).getDescription());
			    ps1.setString(4, adduser.getMenulist().get(i).getValue());
			    ps1.setTimestamp(5, currentTimestamp);
			    ps1.setInt(6,user);
			    ps1.setString(7, adduser.getMenulist().get(i).getDescription());
			    ps1.setString(8, adduser.getMenulist().get(i).getValue());
			    ps1.setTimestamp(9, currentTimestamp);
			    ps1.setInt(10,user);
			    ps1.executeUpdate();//executing query
			    ps1.close();
			}
			
/**********************************************************Adding data in tbl_userPermissions**************************************************/
			for(int i=0;i<menusize;i++)//for storing multiple permissions related to same menuList in database
		    {
		    	int size=adduser.getMenulist().get(i).getPermissions().size();
		    	for(int j=0;j<size;j++)
		    	{
		    		ps1 = conn.prepareStatement(editPermission);
		    		ps1.setInt(1, userid);
			    	ps1.setString(2, adduser.getMenulist().get(i).getId());
			    	ps1.setString(3, adduser.getMenulist().get(i).getPermissions().get(j).getId());
			    	ps1.setString(4, adduser.getMenulist().get(i).getPermissions().get(j).getDescription());
			    	ps1.setString(5, adduser.getMenulist().get(i).getPermissions().get(j).getOverriddenValue());
			    	ps1.setTimestamp(6, currentTimestamp);
			    	ps1.setInt(7,user);
			    	ps1.setString(8, adduser.getMenulist().get(i).getPermissions().get(j).getDescription());
			    	ps1.setString(9, adduser.getMenulist().get(i).getPermissions().get(j).getOverriddenValue());
			    	ps1.setTimestamp(10, currentTimestamp);
			    	ps1.setInt(11,user);
			    	ps1.executeUpdate();//executing query
			    	ps1.close();
		    	}
		    }
				
/**********************************************************Adding data in tbl_user_groups******************************************************/
			ps1=conn.prepareStatement(edituserGroup);
			ps1.setString(1, adduser.getGroup().get(0).getGroupId());
			ps1.setInt(2, userid);
			ps1.executeUpdate();
			ps1.close();
	
/*************************************************************Adding data in user_role********************************************************/
			ps1=conn.prepareStatement(edituserRole);
			ps1.setString(1, adduser.getRoleId());
			ps1.setInt(2, userid);
			ps1.executeUpdate();
			ps1.close();
			
			conn.commit();//Committing all changes to database
			conn.close();		

			success=true;
	        return success;			
		}
		catch (SQLException e) 
		{
			try {
				conn.rollback();//All changes rollBack if any exception occurs
			} catch (SQLException e1) {
			    throw new CustomException(e1.getErrorCode(),e1.getMessage());//throwing custom exception for SQL Exception
			}
		    throw new CustomException(e.getErrorCode(),e.getMessage());//throwing custom exception for SQL Exception
		} 
		catch (CustomException e) 
		{
		    throw new CustomException(e.getErrcode(),e.getErrmsg());//throwing custom exception
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
					throw new CustomException(e.getErrorCode(),e.getMessage());//throwing custom exception
				}
			}
		}
	}
}
