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

import org.apache.commons.lang3.StringUtils;

import bahriskcanvas.User;
/*
 * Class establishes connection and retrieve data from the database and pass the result to respective Controller
 */
public class ConnectionClass 
{
	// This array stores all descendant nodes of a parent
	public static ArrayList<String> children=new ArrayList<String>();
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
	 * @return User 
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws UserException
	 * Method
	 */
	public User getUser(Credentials credentials)throws SQLException,NullPointerException, UserException
	{
		con=dataSource.getConnection();
		int user_id=0;
		/*
		 * Creating User Object to Map with ResultSet data
		 */
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
		PreparedStatement userExist=con.prepareStatement("select id from tbl_user_ticket where user_id=?");
		userExist.setInt(1, user_id);
		ResultSet userExistSet=userExist.executeQuery();
		if(userExistSet.next())
		{
			PreparedStatement updateUserToken=con.prepareStatement("update tbl_user_ticket set alf_ticket=? where id=?");
			updateUserToken.setString(1, user.getAlfTicket());
			updateUserToken.setInt(2, userExistSet.getInt(1));
			int result=updateUserToken.executeUpdate();
			if(result>0)
			{
				con.commit();
				con.close();
			}
		}
		else
		{
		PreparedStatement preparedStatement=con.prepareStatement("insert into tbl_user_ticket(alf_ticket,user_id) values(?,?)");
		preparedStatement.setString(1, user.getAlfTicket());
		preparedStatement.setInt(2,user_id);
		int result=preparedStatement.executeUpdate();
			if(result>0)
			{
				con.commit();
			}
		}
		}
		catch(SQLException e)
		{
			con.rollback();
			throw new UserException(e.getMessage());
		}
		finally
		{
			con.close();
		}
		return user;
	}
	/**
	 * 
	 * @param alfTicket
	 * @return
	 * @throws SQLException
	 * @throws UserException
	 * * Method Delete the row with alfTicket from the table tbl_user_ticket
	 */
	public void logout(String alfTicket)throws SQLException, UserException 
	{
		con=dataSource.getConnection();
		PreparedStatement updateStatement=con.prepareStatement("delete from tbl_user_ticket where alf_ticket=?");
		updateStatement.setString(1,alfTicket);
		int result=updateStatement.executeUpdate();
		con.close();
		if(result>0)
		{
			//do nothing
		}
		else
		{
			throw new UserException("Session no longer Exist! Kindly login again.");
		}
	}
	/**
	 * 
	 * @param creategroup
	 * @return
	 * @throws SQLException
	 * @throws UserException
	 * @throws NullPointerException
	 * Method Creates a New group
	 */
	public boolean getResult(CreateGroup creategroup,String alfTicket)throws SQLException, UserException,NullPointerException,Exception
	{
		con=dataSource.getConnection();
		/*
		 * result is set to true if group creation is successful
		 */
		boolean result=false;
		/*
		 * check for group with neither parent nor children
		 */
		try
		{
			if(creategroup.getIsChild().isEmpty() && creategroup.getIsParent().isEmpty())
			{	
				PreparedStatement insertStatement=con.prepareStatement("insert into tbl_groups(group_id,group_name,parent_id,created_by,created_on)values(?,?,?,?,now())");
				insertStatement.setString(1, "Group_"+creategroup.getGroupName());
				insertStatement.setString(2,creategroup.getGroupName());
				insertStatement.setString(3,"NULL");
				insertStatement.setString(4,new UserId().getUserId(con,alfTicket));
				int insertResult=insertStatement.executeUpdate();
				if(insertResult>0)
				{
					result=true;	
				}
			}
		/*
		 *  check for group with parent but no children
		 */
			else if(creategroup.getIsParent().equals("true") && creategroup.getIsChild().equals("false"))
			{
				if(creategroup.getSelectedGroupName().isEmpty()||creategroup.getSelectedGroupName()==null)
				{
					throw new UserException("SelectedGroupName cannot be left Empty");
				}
				else
				{
					con.setAutoCommit(false);
					PreparedStatement insertStatement=con.prepareStatement("insert into tbl_groups(group_id,group_name,parent_id,created_by,created_on)values(?,?,?,?,now())");
					insertStatement.setString(1, "Group_"+creategroup.getGroupName());
					insertStatement.setString(2,creategroup.getGroupName());
					insertStatement.setString(3,"NULL");
					insertStatement.setString(4,new UserId().getUserId(con,alfTicket));
					int insertResult=insertStatement.executeUpdate();
					insertStatement.close();
					if(insertResult>0)
						{
						PreparedStatement updateStatement=con.prepareStatement("update tbl_groups set parent_id=? where group_id=?"); 
						updateStatement.setString(1, "Group_"+creategroup.getGroupName());
						updateStatement.setString(2, creategroup.getSelectedGroupName());
						int updateResult=updateStatement.executeUpdate();
						if(updateResult>0)
							{
							con.commit();
							result=true;
							}
						else
							{
							throw new UserException("Sorry SelectedGroupName does not exist");
							}
						}
				}
			}
		/*
		 * check if group 
		 */
			else if(creategroup.getIsChild().equals("true") && creategroup.getIsParent().equals("false"))
			{
				if(creategroup.getSelectedGroupName().isEmpty()||creategroup.getSelectedGroupName()==null)
				{
					throw new UserException("SelectedGroupName cannot be left Empty");
				}
				PreparedStatement insertStatement=con.prepareStatement("insert into tbl_groups(group_id,group_name,parent_id,created_by,created_on)values(?,?,?,?,now())");
				insertStatement.setString(1, "Group_"+creategroup.getGroupName());
				insertStatement.setString(2,creategroup.getGroupName());
				insertStatement.setString(3,creategroup.getSelectedGroupName());
				insertStatement.setString(4,new UserId().getUserId(con,alfTicket));
				int insertResult=insertStatement.executeUpdate();
					if(insertResult>0)
					{
						result=true;
					}
					else
					{
						throw new UserException("Sorry SelectedGroupName does not exist");
					}
			}
		}
		catch(SQLException e)
		{
		con.rollback();
		throw new UserException(e.getMessage());
		}
		finally
		{
		con.close();
		}
		return result;
	}
	/**
	 * 
	 * @param editGroup
	 * @return
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws UserException
	 * Method to Edit group
	 */
	public boolean getResult(EditGroup editGroup)throws SQLException,NullPointerException,UserException
	{
		con=dataSource.getConnection();
		/*
		 * result is set to true if editing group is successful
		 */
		boolean result=false;
		PreparedStatement updateStatement=con.prepareStatement("update tbl_groups set group_name=? where group_id=?"); 
		updateStatement.setString(1, "Group_"+editGroup.getGroupName());
		updateStatement.setString(2, editGroup.getGroupId());
		int updateResult=updateStatement.executeUpdate();
		if(updateResult>0)
			{
				result=true;
			}
		else
			{
				result=false;
			}
		con.close();
		return result;
	}
	/**
	 * 
	 * @param group_id
	 * @return
	 * @throws Exception 
	 */
	public boolean getResult(String group_id)throws NullPointerException,UserException,SQLException,Exception
	{
		con=dataSource.getConnection();
		PreparedStatement ps=con.prepareStatement("select group_id from tbl_groups where parent_id=?");
		ps.setString(1,group_id);
		ResultSet rs=ps.executeQuery();
		while(rs.next())
		{
			children.add(rs.getString(1));
		}
		new DescendantChildren().getChildren(children,0,con);
		PreparedStatement findUserStatement=con.prepareStatement("select user_id from tbl_user_groups where group_id IN(?)");
		String parameters = StringUtils.join(children.iterator(),",");  
		ps.setString(1, parameters );  
		ResultSet rSet = findUserStatement.executeQuery();
		if(!rSet.next())
		{
		PreparedStatement deleteGroupStatement=con.prepareStatement("delete from tbl_groups where group_id=?");
		for(String group:children)
		{
			deleteGroupStatement.setString(1, group);
			deleteGroupStatement.addBatch();
		}
		deleteGroupStatement.executeBatch();
		return true;
		}
		else
		{
		return false;
		}
	}
	public boolean getResult(MoveGroup creategroup)
	{
		//code
		return false;
		
	}
}
