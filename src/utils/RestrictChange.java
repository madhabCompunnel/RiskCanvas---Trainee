package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import riskcanvas.exception.CustomException;

public class RestrictChange {
	PreparedStatement ps=null;
	ResultSet rs=null;

	public void restrict(String toRestrict,String toVerify,String toRestrictColumn,String VerifyColumn,String tableName,Connection conn)
	{
		String restrictchange = "SELECT "+toRestrictColumn+" from "+tableName+" WHERE "+VerifyColumn+" ="+"'"+toVerify+"'";
		
		try {
				ps= conn.prepareStatement(restrictchange);
				rs = ps.executeQuery();//executing query
				if(rs.next())
				{
					if(toRestrict.equals(rs.getString(1))){}
				}
				else
					throw new CustomException(505,"EmailId cannot be changed");
			}
		catch (SQLException e) {
			throw new CustomException(e.getErrorCode(),e.getMessage());
		}
		
	}
	
}
