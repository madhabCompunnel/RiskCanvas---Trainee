//data access layer class for dealing with inputs and outputs to and from database
package riskcanvas.dao.authentication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import riskcanvas.dao.DatabaseConnection;
import riskcanvas.exception.CustomException;
import riskcanvas.model.Password;
import utils.CheckTicket;
import utils.CheckValues;

public class AuthenticationDaoImpl implements AuthenticationDao
{
	private DataSource datasource;//for getting database configurations from DatabaseConnection class
	Connection conn=null;//Connection String
	String roleId="ROLE_admin_ju0kpTdLSA";
	PreparedStatement ps=null;
	ResultSet rs=null;
	
	Boolean success=false;
	CheckValues value=new CheckValues();
    CheckTicket checkTicket=new CheckTicket();

    /**
     * for changing and resetting password
     * Only users with role as admin can reset the password 
     */
	@Override
	public Boolean changePassword(String userName,String alfTicket, Password password, DatabaseConnection connection)
	{
		datasource=connection.getDatasource();
		
		//Only one table is required for this service,i.e, tbl_user
		String resetPassword="UPDATE tbl_user SET password=? WHERE username=?";
		String role="SELECT roleId from user_role WHERE user_id=?";
		
		try{
		conn = datasource.getConnection();//creating connection to database
			conn.setAutoCommit(false);
			
			value.checkNull(alfTicket,"Alf_Ticket");
			int user=checkTicket.getticket(alfTicket, conn);
			ps=conn.prepareStatement(role);
			ps.setInt(1, user);
			rs=ps.executeQuery();
			while(rs.next())
			{
				/*********************************************Checking if user is admin******************************************************************/
				if(rs.getString(1).equals(roleId) && password.getOldPassword()==null)
				{
					value.checkNull(password.getNewPassword(),"New Password");
					value.checkNotExist(userName,"tbl_user","username",datasource);
					
					ps=conn.prepareStatement(resetPassword);
					ps.setString(1, password.getNewPassword());
					ps.setString(2, userName);
					ps.executeUpdate();
					ps.close();
					conn.commit();						
				}
				/**************************************users other than admin are allowed to only change password****************************************/
				else if(!rs.getString(1).equals(roleId) && password.getOldPassword()==null &&password.getNewPassword()!=null)
				{
					throw new CustomException(403,"Forbidden:You do not have privileges to reset the password");
				}
				else 
				{
					value.checkNull(password.getOldPassword(),"Old Password");
					value.checkNull(password.getNewPassword(),"New Password");
					value.checkNotExist(userName,"tbl_user","username",datasource);
					value.checkMapping(password.getOldPassword(), userName, "tbl_user","password","username", datasource);
					
					ps=conn.prepareStatement(resetPassword);
					ps.setString(1, password.getNewPassword());
					ps.setString(2, userName);
					ps.executeUpdate();
					ps.close();
					conn.commit();//commiting transactions
				}
			}
			ps.close();
			
			
			
			
			success=true;
			return success;
		}
		catch(SQLException e)
		{
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new CustomException(e.getErrorCode(),e.getMessage());
			}
			throw new CustomException(e.getErrorCode(),e.getMessage());
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
