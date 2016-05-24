package riskcanvas.dao.group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import riskcanvas.exception.CustomException;
import riskcanvas.dao.GetConfig;
import riskcanvas.dao.DatabaseConnection;
import riskcanvas.model.EditGroup;
import utils.CheckValues;

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
			DataSource dataSource=databaseConnection.getDatasource();
			con=databaseConnection.getDatasource().getConnection();
			new CheckValues().checkNotExist(alfTicket, "tbl_user_ticket","alf_ticket",dataSource);
			boolean result=false;
			PreparedStatement updateStatement=con.prepareStatement("update tbl_groups set group_name=? where group_id=?"); 
			updateStatement.setString(1,editGroup.getGroupName());
			updateStatement.setString(2, editGroup.getGroupId());
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
			e.printStackTrace();
			throw new CustomException(400,e.getMessage());
		}
	}
			
}
