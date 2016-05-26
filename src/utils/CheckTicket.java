//class for getting alf_ticket
package utils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import riskcanvas.exception.CustomException;

/**
 * @param alf_ticket
 * @return user
 * @throws SQLException
 * method for getting user associated with the input ticket
 */
public class CheckTicket {
	int user;
	public int getticket(String alf_ticket,Connection conn) {
		String sql1 = "SELECT user_id from tbl_user_ticket WHERE alf_ticket='"+alf_ticket+"'";
		try{
			PreparedStatement ps5 = conn.prepareStatement(sql1);
			ResultSet rs = ps5.executeQuery();//executing query
			if(rs.next())
			{
			user=rs.getInt(1);
			return user;
			}
			else
			{
				throw new CustomException(505,"No such token id exists :"+alf_ticket);
			}
		}
		catch(SQLException e)
		{
			throw new CustomException(e.getErrorCode(),e.getMessage());
		}	
	}	
}

