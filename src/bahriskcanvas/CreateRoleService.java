/**
 * @author 13089
 * @date 13/5/2016
 * @time 10:37 AM
 *
 */
//main service for controlling and manipulating database based on the the JSON input from user
//
package bahriskcanvas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import javax.sql.DataSource;

import bahriskcanvas.CheckRoleName;
import bahriskcanvas.CreateRoleInput;
import bahriskcanvas.CreateRoleOutput;
import bahriskcanvas.customException;
import bahriskcanvas.getalf_ticket;


public class CreateRoleService 
{
	private DataSource datasource;//Spring's own class for setting database related configuration in Beans.xml file

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
	public boolean insert(CreateRoleInput createinput,int menusize,DatabaseConnection conndata) throws SQLException
	{
		Boolean success;
		datasource=conndata.getDatasource();
		CheckRoleName checkrolename=new CheckRoleName();//method for checking if roleName already exists or if roleId(if already exists) is legitimate
		checkrolename.CheckAlreadyExist(null,createinput.getRoleName(),datasource);
		
	    /**
	     * Three tables are managed for various field of input
	     */
		String createrole = "INSERT INTO tbl_createrole (roleid,roleName,isActive,roleType,defaultScreen,createdon,createdby) VALUES (?, ?, ?, ?, ? , ?, ?)";
		String addmenulist = "INSERT INTO tbl_menulist (roleid,id,description,value,createdon,createdby) VALUES (?, ?, ? , ?, ?, ?)";
		String addpermission = "INSERT INTO tbl_permissions (roleid,menuid,id,description,value,createdon,createdby) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
			/*************************************************Adding data to tbl_createrole******************************************************/
			conn = datasource.getConnection();//creating connection to database
			conn.setAutoCommit(false);
			PreparedStatement ps1 = conn.prepareStatement(createrole);
			ps1.setString(1, roleid);
			ps1.setString(2, createinput.getRoleName());
			ps1.setString(3, createinput.getIsActive());
			ps1.setString(4, createinput.getRoleType());
			ps1.setString(5, createinput.getDefaultScreen());
			ps1.setTimestamp(6, currentTimestamp);
			ps1.setString(7,user);
			ps1.executeUpdate();//executing query
			ps1.close();

			/*************************************************Adding data to tbl_menulist******************************************************/
			ps1 = conn.prepareStatement(addmenulist);
			for(int i=0;i<menusize;i++)
			{
		    ps1.setString(1, roleid);
		    ps1.setString(2, createinput.getMenulist().get(i).getId());
		    ps1.setString(3, createinput.getMenulist().get(i).getDescription());
		    ps1.setString(4, createinput.getMenulist().get(i).getValue());
		    ps1.setTimestamp(5, currentTimestamp);
		    ps1.setString(6,user);
		    ps1.executeUpdate();//executing query
			}
			ps1.close();
		    
			/*************************************************Adding data to tbl_permissions******************************************************/
			ps1 = conn.prepareStatement(addpermission);
			for(int i=0;i<menusize;i++)//for storing multiple permissions related to same menuList in database
		    {
		    	int size=createinput.getMenulist().get(i).getPermissions().size();
		    	for(int j=0;j<size;j++)
		    	{
		    	ps1.setString(1, roleid);
		    	ps1.setString(2, createinput.getMenulist().get(i).getId());
		    	ps1.setString(3, createinput.getMenulist().get(i).getPermissions().get(j).getId());
		    	ps1.setString(4, createinput.getMenulist().get(i).getPermissions().get(j).getDescription());
		    	ps1.setString(5, createinput.getMenulist().get(i).getPermissions().get(j).getValue());
		    	ps1.setTimestamp(6, currentTimestamp);
		    	ps1.setString(7,user);
		    	ps1.executeUpdate();//executing query
		    	}
		    }
			ps1.close();
			
		    conn.commit();//Committing all transactions
		    conn.close();
		    output.setSuccess(true);
		    success=output.getSuccess();
		    return success;    
		} 
		catch (SQLException e) 
		{
			conn.rollback();
		    throw new customException(e.getErrorCode(),e.getMessage());//throwing custom exception
		} 
		
	}

/*****************************************************************************************************************************************************/	
/*******************************************************       FOR EDITING ROLE      *****************************************************************/
/*****************************************************************************************************************************************************/
	public boolean update(CreateRoleInput roleinput,int menusize,DatabaseConnection conndata) throws SQLException
	{

		Boolean success;//variable for sending true/false as output
		datasource=conndata.getDatasource();
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
		String updaterole = "UPDATE tbl_createrole SET roleName=?,isActive=?,defaultScreen=?,updatedon=?,updatedby=? WHERE roleid=?";
		String updateMenulist = "INSERT INTO tbl_menulist (roleid,id,description,value,createdon,createdby) VALUES (?, ?, ? , ?, ?, ?) ON DUPLICATE KEY UPDATE description=?,value=?,updatedon=?,updatedby=?";
		String updatePermissions = "INSERT INTO tbl_permissions (roleid,menuid,id,description,value,createdon,createdby) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE description=?,value=?,updatedon=?,updatedby=?";
		String updateRoleId=  "UPDATE tbl_createrole SET roleId=? WHERE roleName=?";
		
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
			/*************************************************Adding data to tbl_createrole******************************************************/
			conn = datasource.getConnection();//creating connection to database
			conn.setAutoCommit(false);
			PreparedStatement ps1 = conn.prepareStatement(updaterole);
			ps1.setString(1, roleinput.getRoleName());
			ps1.setString(2, roleinput.getIsActive());
			ps1.setString(3, roleinput.getDefaultScreen());
			ps1.setTimestamp(4, currentTimestamp);
			ps1.setString(5,user);
			ps1.setString(6, roleinput.getRoleId());
			ps1.executeUpdate();//executing query
			ps1.close();
		    
			/*************************************************Adding data to tbl_menulist******************************************************/
		    for(int i=0;i<menusize;i++)
			{
		    ps1 = conn.prepareStatement(updateMenulist);
		    ps1.setString(1, roleid);
		    ps1.setString(2, roleinput.getMenulist().get(i).getId());
		    ps1.setString(3, roleinput.getMenulist().get(i).getDescription());
		    ps1.setString(4, roleinput.getMenulist().get(i).getValue());
		    ps1.setTimestamp(5, currentTimestamp);
		    ps1.setString(6,user);
		    ps1.setString(7, roleinput.getMenulist().get(i).getDescription());
		    ps1.setString(8, roleinput.getMenulist().get(i).getValue());
		    ps1.setTimestamp(9, currentTimestamp);
		    ps1.setString(10,user);
		    ps1.executeUpdate();//executing query
		    ps1.close();
			}
		    
		    /*************************************************Adding data to tbl_permissions******************************************************/
	    	ps1 = conn.prepareStatement(updatePermissions);
		    for(int i=0;i<menusize;i++)//for storing multiple permissions related to same menuList in database
		    {
		    	int size=roleinput.getMenulist().get(i).getPermissions().size();
		    	for(int j=0;j<size;j++)
		    	{
		    	ps1.setString(1, roleid);
		    	ps1.setString(2, roleinput.getMenulist().get(i).getId());
		    	ps1.setString(3, roleinput.getMenulist().get(i).getPermissions().get(j).getId());
		    	ps1.setString(4, roleinput.getMenulist().get(i).getPermissions().get(j).getDescription());
		    	ps1.setString(5, roleinput.getMenulist().get(i).getPermissions().get(j).getValue());
		    	ps1.setTimestamp(6, currentTimestamp);
		    	ps1.setString(7,user);
		    	ps1.setString(8, roleinput.getMenulist().get(i).getPermissions().get(j).getDescription());
		    	ps1.setString(9, roleinput.getMenulist().get(i).getPermissions().get(j).getValue());
		    	ps1.setTimestamp(10, currentTimestamp);
		    	ps1.setString(11,user);
		    	ps1.executeUpdate();//executing query
		    	}
		    }
		    ps1.close();
		    
		    /************************************************Renaming roleId******************************************************/
		    roleinput.setRoleId(newroleid.toString());//setting new roleId
		    ps1 = conn.prepareStatement(updateRoleId);//for renaming roleId as per changed roleName
		    ps1.setString(1, roleinput.getRoleId() );
		    ps1.setString(2, roleinput.getRoleName());
		    ps1.executeUpdate();//executing query
		    ps1.close();
		    
		    conn.commit();//Committing all transactions
		    conn.close();
		    output.setSuccess(true);
		    success=output.getSuccess();
		    return success;    
		} 
		catch (SQLException e) 
		{
			conn.rollback();
		    throw new customException(e.getErrorCode(),e.getMessage());//throwing custom exception
		} 
	}
/*****************************************************************************************************************************************************/	
/*******************************************************       FOR LISTING ROLE      *****************************************************************/
/*****************************************************************************************************************************************************/

	public Roles list(String excludeInactive,DatabaseConnection conndata,String ticket) throws SQLException
	{
		datasource=conndata.getDatasource();
		getalf_ticket getalfticket=new getalf_ticket();//getting current user alf_ticket
		@SuppressWarnings("unused")
		String user=getalfticket.getticket(ticket, datasource, conn);
		Roles roles=new Roles();//variable that will be returned in output
		ArrayList<String> roleidlist=new ArrayList<String>();//arrayList for storing all roleid's contained in database
		ArrayList<String> menuidlist=new ArrayList<String>();//arrayList for storing all menuid's contained in database
		ArrayList<CreateRoleInput> rolelist=new ArrayList<CreateRoleInput>();//initial arrayList for storing all roleId's,roleName's,assignedUsers,isActive,defaultScreen of stored roles 
		ArrayList<CreateRoleInput> rolelistinter=new ArrayList<CreateRoleInput>();//intermediate list for storing above detail plus menuLists related to respected roles
		ArrayList<CreateRoleInput> rolelistfinal=new ArrayList<CreateRoleInput>();//final list of roles,menuLists and permissions
		ArrayList<Menulist> menulist=new ArrayList<Menulist>();//initial arrayList for storing all menuLists
		ArrayList<Menulist> menulistfinal=new ArrayList<Menulist>();//final menuList for storing menuList's plus related permissions
		ArrayList<RolePermission> permissionlist=new ArrayList<RolePermission>();//arraylist for storing all permissions
		HashMap<String,ArrayList<String>> roleMenuIdMap=new HashMap<String,ArrayList<String>>();//hashMap for mapping roleid's to menuid's
		
		/**
		 * Three tables are managed for RoleService so following queries on three tables
		 */
		String listOnlyActive="SELECT isActive,roleName,roleId,assignedUsers,defaultScreen from tbl_createrole where isActive='true'";
		String listAll="SELECT isActive,roleName,roleId,assignedUsers,defaultScreen from tbl_createrole";
		String listMenulist="SELECT id,description,value from tbl_menulist WHERE roleId=?";
		String listPermissions="SELECT id,description,value from tbl_permissions WHERE roleId=? AND menuid=?";
		
		try{
			int index,menulistFirstIndex=0,menulistfinalFirstIndex=0,permissionFirstIndex=0;
			conn=datasource.getConnection();
			conn.setAutoCommit(false);
			
			PreparedStatement ps=null;
			if(excludeInactive.equals("false"))
		    	{ps=conn.prepareStatement(listAll);}
			else
				{ps=conn.prepareStatement(listOnlyActive);}
/**********************************************       retrieving data related to role     ************************************************************/
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				roleidlist.add(rs.getString(3));
				rolelist.add(new CreateRoleInput(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));
			}
			rs.close();		
/*********************************************     retrieving data related to menuLists     **********************************************************/
		    ps=conn.prepareStatement(listMenulist);
		    for(index=0;index<roleidlist.size();index++){
				ps.setString(1, roleidlist.get(index));
				ResultSet rs1=ps.executeQuery();
				while(rs1.next()){
				   menuidlist.add(rs1.getString(1));
				   menulist.add(new Menulist(rs1.getString(1),rs1.getString(2),rs1.getString(3)));
				}	
				ArrayList<Menulist> al2 = new ArrayList<Menulist>(menulist.subList(menulistFirstIndex, menulist.size()));
				ArrayList<String> al1 = new ArrayList<String>(menuidlist.subList(menulistFirstIndex, menuidlist.size()));
				roleMenuIdMap.put(roleidlist.get(index),al1 );//role+menuId hashMap
				rolelistinter.add(new CreateRoleInput(rolelist.get(index).getIsActive(),rolelist.get(index).getRoleName(),rolelist.get(index).getRoleId(),rolelist.get(index).getAssignedUser(),rolelist.get(index).getDefaultScreen(),al2));
				menulistFirstIndex=menulist.size();	
				rs1.close();
			}
/*********************************************     retrieving data related to permissions     ********************************************************/
			ps=conn.prepareStatement(listPermissions);
			for(index=0;index<roleMenuIdMap.size();index++){
				int j=roleMenuIdMap.get(roleidlist.get(index)).size();//returns size of an arraylist of menuid's for corresponding key of roleid
				for(int k=0;k<j;k++){
				ps.setString(1,roleidlist.get(index));
				ps.setString(2, roleMenuIdMap.get(roleidlist.get(index)).get(k));
				ResultSet rs2=ps.executeQuery();
				while(rs2.next()){
					permissionlist.add(new RolePermission(rs2.getString(1),rs2.getString(2),rs2.getString(3)));
				}
				ArrayList<RolePermission> al2 = new ArrayList<RolePermission>(permissionlist.subList(permissionFirstIndex, permissionlist.size()));
				menulistfinal.add(new Menulist(rolelistinter.get(index).getMenulist().get(k).getId(),rolelistinter.get(index).getMenulist().get(k).getDescription(),rolelistinter.get(index).getMenulist().get(k).getValue(),al2));
				permissionFirstIndex=permissionlist.size();
				rs2.close();
				}
				ArrayList<Menulist> al3 = new ArrayList<Menulist>(menulistfinal.subList(menulistfinalFirstIndex, menulistfinal.size()));
				rolelistfinal.add(new CreateRoleInput(rolelistinter.get(index).getIsActive(),rolelistinter.get(index).getRoleName(),rolelistinter.get(index).getRoleId(),rolelistinter.get(index).getAssignedUser(),rolelistinter.get(index).getDefaultScreen(),al3));
				menulistfinalFirstIndex=menulistfinal.size();
			}
			ps.close();
			
			conn.commit();//Committing all transactions
			conn.close();
			roles.setRoles(rolelistfinal);//setting final result data to roles for providing required output
			return roles;
		}
		catch (SQLException e) 
		{
			conn.rollback();
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