package riskcanvas.dao.group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import riskcanvas.exception.CustomException;
import riskcanvas.dao.GetConfig;
import riskcanvas.dao.DatabaseConnection;
import riskcanvas.model.EditGroup;
import utils.CheckTicket;

/**
 * 
 * @author 13083
 *
 */
public class GroupDaoImpl implements GroupDao 
{
	private Connection con;
	
	@Override
	public boolean editGroup(EditGroup editGroup,HttpServletRequest request,String alfTicket)
	{
	try
	{
		/*
	 	* result is set to true if editing group is successful
	 	*/
		DatabaseConnection databaseConnection=GetConfig.getConnection(request);
		con=databaseConnection.getDatasource().getConnection();
		int user_id=new CheckTicket().getticket(alfTicket, con);
		boolean result=false;
		PreparedStatement updateStatement=con.prepareStatement("UPDATE tbl_groups SET group_name=?, updated_by=? WHERE group_id=?"); 
		updateStatement.setString(1,editGroup.getGroupName());
		updateStatement.setInt(2,user_id);
		updateStatement.setString(3, editGroup.getGroupId());
		int updateResult=updateStatement.executeUpdate();
		if(updateResult>0)
		{
			result=true;
		}
		else
		{
			throw new CustomException(400,"Check if group Id is correct!");
		}
		con.close();
		return result;
	}
	catch(SQLException e)
	{
		e.printStackTrace();
		throw new CustomException(e.getErrorCode(),e.getMessage());
	}
	catch(NullPointerException e)
	{
		throw new CustomException(400,e.getMessage());
	}
}
	
	
	public boolean moveGroup(EditGroup editGroup,HttpServletRequest request,String alfTicket)
	{
	try
		{
			/*
		 	* result is set to true if editing group is successful
		 	*/
			boolean result=false;
			DatabaseConnection databaseConnection=GetConfig.getConnection(request);
			con=databaseConnection.getDatasource().getConnection();
			int user_id=new CheckTicket().getticket(alfTicket, con);
			PreparedStatement updateStatement=con.prepareStatement("UPDATE tbl_groups SET parent_id=?,updated_by=? WHERE group_id=?"); 
			updateStatement.setString(1,editGroup.getDestinationGroupId());
			updateStatement.setInt(2,user_id);
			updateStatement.setString(3, editGroup.getGroupId());
			int updateResult=updateStatement.executeUpdate();
			if(updateResult>0)
				{
					result=true;
				}
			else
				{
					throw new CustomException(400,"Check if fields are correct!");
				}
			con.close();
			return result;
		}
		catch(SQLException e)
		{
		throw new CustomException(e.getErrorCode(),e.getMessage());
		}
		catch(NullPointerException e)
		{
			throw new CustomException(400,e.getMessage());
		}
	}
			
}
