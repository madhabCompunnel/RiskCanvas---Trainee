package riskcanvas.dao.group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import riskcanvas.exception.UserException;
import bahriskcanvas.UserId;
import riskcanvas.dao.GetConfig;
import riskcanvas.dao.DatabaseConnection;
import riskcanvas.model.EditGroup;

public class GroupDaoImpl implements GroupDao 
{
	Connection con;
	
	@Override
	public boolean editGroup(EditGroup editGroup,HttpServletRequest request,String alfTicket)
	{
		if(new UserId().getUserId(con,alfTicket)!=0)
		{
		try
		{
			DatabaseConnection databaseConnection=GetConfig.getConnection(request);
			con=databaseConnection.getDatasource().getConnection();
				
						/*
						 * result is set to true if editing group is successful
						 */
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
								result=false;
							}
						con.close();
						return result;
					}
			catch(SQLException e)
			{
			throw new UserException(e.getMessage());
			}
			catch(Exception e)
			{
				throw new UserException(e.getMessage());
			}
		}
		else
		{
			throw new UserException("Session Expired! login to continue");
		}
			
	}
}
