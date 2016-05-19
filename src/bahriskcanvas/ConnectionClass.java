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
	public  ArrayList<String> children=new ArrayList<String>();
	public  ArrayList<Group> subgroup=null; 
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
		PreparedStatement selectUserStatement=con.prepareStatement("SELECT U.USERNAME, U.F_NAME, U.L_NAME, CR.ROLENAME, CR.DEFAULTSCREEN, GROUP_CONCAT(P.ID), GROUP_CONCAT(P.DESCRIPTION), GROUP_CONCAT(P.VALUE),U.PASSWORD,U.USER_ID FROM TBL_USER AS U INNER JOIN USER_ROLE AS UR ON U.USER_ID=UR.USER_ID INNER JOIN TBL_CREATEROLE AS CR ON CR.ROLEID=UR.ROLEID INNER JOIN TBL_PERMISSIONS AS P ON UR.ROLEID=P.ROLEID WHERE USERNAME=?");
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
				if(((pval[index]).toString()).equals("true"))
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
			System.out.println(permissionList);
		}
		/*
		 * Generating Unique ID for the user	
		 */
		UUID uniqueKey = UUID.randomUUID();
		user.setAlfTicket("TICKET_"+uniqueKey.toString());
		PreparedStatement userExist=con.prepareStatement("SELECT ID FROM TBL_USER_TICKET WHERE USER_ID=?");
		userExist.setInt(1, user_id);
		ResultSet userExistSet=userExist.executeQuery();
		if(userExistSet.next())
		{
			PreparedStatement updateUserToken=con.prepareStatement("UPDATE TBL_USER_TICKET SET ALF_TICKET=? WHERE ID=?");
			updateUserToken.setString(1, user.getAlfTicket());
			updateUserToken.setInt(2, userExistSet.getInt(1));
			int result=updateUserToken.executeUpdate();
			if(result>0)
			{
				con.commit();
			}
		}
		else
		{
		PreparedStatement preparedStatement=con.prepareStatement("INSERT INTO TBL_USER_TICKET(ALF_TICKET,USER_ID) VALUES(?,?)");
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
		PreparedStatement updateStatement=con.prepareStatement("DELETE FROM TBL_USER_TICKET WHERE ALF_TICKET=?");
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
			con.setAutoCommit(false);
			if(creategroup.getIsChild().isEmpty() && creategroup.getIsParent().isEmpty())
			{	
				PreparedStatement insertStatement=con.prepareStatement("INSERT INTO TBL_GROUPS(GROUP_ID,GROUP_NAME,PARENT_ID,CREATED_BY,CREATED_ON)VALUES(?,?,?,?,NOW())");
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
					PreparedStatement insertStatement=con.prepareStatement("INSERT INTO TBL_GROUPS(GROUP_ID,GROUP_NAME,PARENT_ID,CREATED_BY,CREATED_ON)VALUES(?,?,?,?,NOW())");
					insertStatement.setString(1, "Group_"+creategroup.getGroupName());
					insertStatement.setString(2,creategroup.getGroupName());
					insertStatement.setString(3,"NULL");
					insertStatement.setString(4,new UserId().getUserId(con,alfTicket));
					int insertResult=insertStatement.executeUpdate();
					insertStatement.close();
					if(insertResult>0)
						{
						PreparedStatement updateStatement=con.prepareStatement("UPDATE TBL_GROUPS SET PARENT_ID=? WHERE GROUP_ID=?"); 
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
		 * check for group with no parent but children
		 */
			else if(creategroup.getIsChild().equals("true") && creategroup.getIsParent().equals("false"))
			{
				if(creategroup.getSelectedGroupName().isEmpty()||creategroup.getSelectedGroupName()==null)
				{
					throw new UserException("SelectedGroupName cannot be left Empty");
				}
				PreparedStatement insertStatement=con.prepareStatement("INSERT INTO TBL_GROUPS(GROUP_ID,GROUP_NAME,PARENT_ID,CREATED_BY,CREATED_ON)VALUES(?,?,?,?,NOW())");
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
		PreparedStatement updateStatement=con.prepareStatement("UPDATE TBL_GROUPS SET GROUP_NAME=? WHERE GROUP_ID=?"); 
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
		PreparedStatement ps=con.prepareStatement("SELECT GROUP_ID FROM TBL_GROUPS WHERE PARENT_ID=?");
		ps.setString(1,group_id);
		ResultSet rs=ps.executeQuery();
		while(rs.next())
		{
			children.add(rs.getString(1));
		}
		new DescendantChildren().getChildren(children,0,con);
		PreparedStatement findUserStatement=con.prepareStatement("SELECT USER_ID FROM TBL_USER_GROUPS WHERE GROUP_ID IN(?)");
		String parameters = StringUtils.join(children.iterator(),",");  
		ps.setString(1, parameters );  
		ResultSet rSet = findUserStatement.executeQuery();
		if(!rSet.next())
		{
		PreparedStatement deleteGroupStatement=con.prepareStatement("DELETE FROM TBL_GROUPS WHERE GROUP_ID=?");
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
	public ArrayList<Group> getResult()throws Exception
	{
		subgroup=new ArrayList<Group>();
		con=dataSource.getConnection();
		Groups groups=new Groups();
		/*PreparedStatement getParentStatement=con.prepareStatement("select group_id,group_name,parent_id from tbl_groups where parent_id=?");
	
		getParentStatement.setString(1,"NULL");
		ResultSet parents=getParentStatement.executeQuery();
		while(parents.next())//America,Asia
		{
			Group group=new Group();
			group.setGroupId(parents.getString(1));
			group.setGroup_name(parents.getString(2));
			group.setParentId(parents.getString(3)); */
			PreparedStatement ps=con.prepareStatement("select group_id,group_name,parent_id from tbl_groups where parent_id=?");
			ps.setString(1,"Group_America");
			ResultSet rs=ps.executeQuery();
			while(rs.next())//South,North
			{
				Group sgroup=new Group();
				sgroup.setGroupId(rs.getString(1));
				sgroup.setGroup_name(rs.getString(2));
				sgroup.setParentId(rs.getString(3));
				subgroup.add(sgroup);
			}
			getChildren(subgroup,0);
		//}
		//groups.setGroup(subgroup);
			con.close();
		return subgroup;
		}
			public void getChildren(ArrayList<Group> children,int size) throws Exception
			{
				for(int i=size;i<children.size();i++)
				{
					ArrayList<Group> temp=new ArrayList<Group>();
					PreparedStatement ps=con.prepareStatement("select group_id,group_name,parent_id from tbl_groups where parent_id=?");
					ps.setString(1, children.get(i).getGroupId());
					ResultSet rs=ps.executeQuery();
					while(rs.next())
					{
						Group sgroup=new Group();
						sgroup.setGroupId(rs.getString(1));
						sgroup.setGroup_name(rs.getString(2));
						sgroup.setParentId(rs.getString(3));
						temp.add(sgroup);
					}
					children.get(i).setSubgroup(temp);
					getSubChildren(temp,0);
					getChildren(children,children.size()-size);
				}		
			}
			public void getSubChildren(ArrayList<Group> temp,int size)throws Exception
			{
				
				for(int i=size;i<temp.size();i++)
				{
					PreparedStatement ps=con.prepareStatement("select group_id,group_name,parent_id from tbl_groups where parent_id=?");
					ps.setString(1, temp.get(i).getGroupId());
					ResultSet rs=ps.executeQuery();
					ArrayList<Group> temp1=new ArrayList<Group>();
					while(rs.next())
					{
						
						Group g=new Group();
						g.setGroupId(rs.getString(1));
						g.setGroup_name(rs.getString(2));
						g.setParentId(rs.getString(3));
						temp1.add(g);
						
					}
						temp.get(i).setSubgroup(temp1);
						getSubChildren(temp1,0);
						getChildren(temp,temp.size()-size);
					
					}
				}
				
			
}
