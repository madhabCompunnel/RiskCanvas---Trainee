/*
 * This Program is used to fetch the Data from the Database.
 */
package bahriskcanvas;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import javax.sql.DataSource;

import bahriskcanvas.User;
/*
 * Class establishes connection and retrieve data from the database
 */
public class ConnectionClass 
{
	private DataSource dataSource=null;
	private Connection con=null;
	/**
	 * 
	 * @param dataSource
	 * setDataSource method is used to initialize the DataSource Object
	 */
	public void setDataSource(DataSource dataSource) 
	{
		this.dataSource = dataSource;
	}
	/**
	 * 
	 * @param credentials
	 * @return
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws UserException
	 */
	public User getUser(Credentials credentials)throws SQLException,NullPointerException, UserException
	{
		try
		{
		con=dataSource.getConnection();
		}
		catch(SQLException e)
		{
			throw new UserException(e.getMessage());
		}
		int user_id=0;
		User user=new User();
		PreparedStatement selectUserStatement=con.prepareStatement("select u.username, u.f_name, u.l_name, cr.roleName, cr.defaultscreen, GROUP_CONCAT(p.id), GROUP_CONCAT(p.description), GROUP_CONCAT(p.value),u.password,u.user_id FROM tbl_user as u INNER JOIN user_role as ur ON u.user_id=ur.user_id INNER JOIN tbl_createrole as cr ON cr.roleId=ur.roleId INNER JOIN tbl_permissions as p ON ur.roleId=p.roleid WHERE username=?");
		selectUserStatement.setString(1,credentials.getUsername());
		/*
		 * Executing query and getting result into the ResultSet Object
		 */
		try
		{
		con.setAutoCommit(false);
		ResultSet userResult=selectUserStatement.executeQuery();
		while(userResult.next())
		{
			
			user_id=userResult.getInt(10);
			if(!(userResult.getString(9).equals(credentials.getPassword())))
			{
				throw new UserException("password does not match");
			}
			/*
			 * Set User object properties with ResultSet data
			 */
			user.setUserName(userResult.getString(1));
			user.setFName(userResult.getString(2));
			user.setLName(userResult.getString(3));
			user.setRole(userResult.getString(4));
			user.setDefaultScreen(userResult.getString(5));
			String [] pid=userResult.getString(6).split(",");
			String [] pdesc=userResult.getString(7).split(",");
			String [] pval=userResult.getString(8).split(",");
			ArrayList<Permission> permissionList=new ArrayList<Permission>();
			boolean value=false;
			for(int index=0;index<pid.length;index++)
			{	
				System.out.println(pval[index]);
				if(Integer.parseInt((pval[index]).toString())==1)
				{
					value=true;
				}
				else
				{
					value=false;
				}
				permissionList.add(new Permission(pid[index],pdesc[index],value));
			}
			user.setPermissions(permissionList);
		}
		/*
		 * Generating Unique ID for the user	
		 */
		UUID uniqueKey = UUID.randomUUID();
		user.setAlfTicket("TICKET_"+uniqueKey.toString());
		PreparedStatement preparedStatement=con.prepareStatement("insert into tbl_user_ticket(alf_ticket,user_id) values(?,?)");
		preparedStatement.setString(1, user.getAlfTicket());
		preparedStatement.setInt(2,user_id);
		int result=preparedStatement.executeUpdate();
		if(result>0)
		{
			//do nothing
		}
		con.commit();
		}
		catch(SQLException e)
		{
			con.rollback();
			throw new UserException(e.getMessage());
		}
		return user;
	}
	/**
	 * 
	 * @param alfTicket
	 * @return
	 * @throws SQLException
	 * @throws UserException
	 * * Method Delete the row with alfTicket 
	 */
	public void logout(String alfTicket)throws SQLException, UserException 
	{
		try
		{
		con=dataSource.getConnection();
		}
		catch(SQLException e)
		{
			throw new UserException(e.getMessage());
		}
		PreparedStatement updateStatement=con.prepareStatement("delete from tbl_user_ticket where alf_ticket=?");
		updateStatement.setString(1,alfTicket);
		int result=updateStatement.executeUpdate();
		if(result>0)
		{
			//do nothing
		}
		else
		{
			throw new UserException("Session no longer Exist! Kindly login again.");
		}
	}
	public boolean getResult(CreateGroup creategroup)throws SQLException
	{
		boolean result=false;
		try
		{
			/*
			 * establishing connection
			 */
		con=dataSource.getConnection();
		System.out.println("established");
		}
		catch(Exception e)
		{
			System.out.println("not established");
			e.printStackTrace();
		}
		if(creategroup.getIsChild().isEmpty() && creategroup.getIsParent().isEmpty())
		{
			PreparedStatement insertStatement=con.prepareStatement("insert into tbl_groups values(?,?,?)");
			insertStatement.setString(1, "Group_"+creategroup.getGroupName());
			insertStatement.setString(2,creategroup.getGroupName());
			insertStatement.setString(3,"NULL");
			int insertResult=insertStatement.executeUpdate();
			if(insertResult>0)
			{
				System.out.println("new group added");
				result=true;
				
			}
			else
			{
				System.out.println("failed to add the group");
				result=false;
			}
			
		}
		if(creategroup.getIsParent()=="true" && creategroup.getIsChild()=="false")
		{
			try
			{
				con.setAutoCommit(false);
				PreparedStatement insertStatement=con.prepareStatement("insert into tbl_groups values(?,?,?)");
				insertStatement.setString(1, "Group_"+creategroup.getGroupName());
				insertStatement.setString(2,creategroup.getGroupName());
				insertStatement.setString(3,"NULL");
				int insertResult=insertStatement.executeUpdate();
				insertStatement.close();
				if(insertResult>0)
					{
						System.out.println("parent insert successful");
					}
				else
					{
						System.out.println("unable to insert parent");
					}
				PreparedStatement updateStatement=con.prepareStatement("update tbl_groups set parent_id=? where group_id=?"); 
				updateStatement.setString(1, "Group_"+creategroup.getGroupName());
				updateStatement.setString(2, creategroup.getSelectedGroupName());
				int updateResult=updateStatement.executeUpdate();
				if(updateResult>0)
					{
						System.out.println("child updated");
					}
				else
					{
						System.out.println("failed to update the child");
					}
				con.commit();
				result=true;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				con.rollback();
				result=false;
			}
		}
		else if(creategroup.getIsChild()=="true" && creategroup.getIsParent()=="false")
		{
			PreparedStatement preparedStatement=con.prepareStatement("insert into tbl_groups values(?,?,?)");
			preparedStatement.setString(1, "Group_"+creategroup.getGroupName());
			preparedStatement.setString(2,creategroup.getGroupName());
			preparedStatement.setString(3,creategroup.getSelectedGroupName());
			int insertResult=preparedStatement.executeUpdate();
				if(insertResult>0)
				{
					System.out.println("successful creating group");
					result=true;
				}
				else
				{
					System.out.println("unable to create a group");
					result=false;
				}
		}
		return result;
	}
	public boolean getResult(EditGroup editGroup)throws SQLException
	{
		try
		{
			/*
			 * establishing connection
			 */
		con=dataSource.getConnection();
		System.out.println("established");
		}
		catch(Exception e)
		{
			System.out.println("not established");
			e.printStackTrace();
		}
		boolean result=false;
		PreparedStatement updateStatement=con.prepareStatement("update tbl_groups set group_name=? where group_id=?"); 
		updateStatement.setString(1, "Group_"+editGroup.getGroupName());
		updateStatement.setString(2, editGroup.getGroupId());
		int updateResult=updateStatement.executeUpdate();
		if(updateResult>0)
			{
				System.out.println("group updated");
				result=true;
			}
		else
			{
				System.out.println("failed to update the group");
				result=false;
			}
		
		return result;
		
	}
	public boolean getResult(String group_id)throws SQLException
	{
		try
		{
			/*
			 * establishing connection
			 */
		con=dataSource.getConnection();
		System.out.println("established");
		}
		catch(Exception e)
		{
			System.out.println("not established");
			e.printStackTrace();
		}
		ArrayList<Group> groups=new ArrayList<Group>();
	//	int [] childCount=null;
		int index=0;
		while(group_id!=null)
		{
			//get parent
			PreparedStatement selectParentStatement=con.prepareStatement("select group_id,group_name,parent_id from tbl_groups where group_id=?");
			selectParentStatement.setString(1, group_id);
			ResultSet parent=selectParentStatement.executeQuery();
			while(parent.next())
			{
				groups.add(new Group(parent.getString(1),parent.getString(2),parent.getString(3)));
			}
					//get children
				PreparedStatement getChildStatement=con.prepareStatement("select group_id,group_name,parent_id from tbl_groups where parent_id=?");
				getChildStatement.setString(1, group_id);
				ResultSet childSet=selectParentStatement.executeQuery();
				
			
				while(childSet.next())
				{
					groups.add(new Group(childSet.getString(1),childSet.getString(2),childSet.getString(3)));
					
				}
			}
			group_id=groups.get(index+1).getParent_id();				
		return false;
	}
	public boolean getResult(MoveGroup creategroup)
	{
		
		//code
		return false;
		
	}
}
