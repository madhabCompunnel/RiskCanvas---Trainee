//class for verifying and validation various values required for processing output
package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import riskcanvas.exception.CustomException;

public class CheckValues {
	PreparedStatement ps=null;
	ResultSet rs=null;
	
	/**
	 * @param toverify
	 * @param tablename
	 * @param columnName
	 * @param datasource
	 * @throws SQLException
	 * @throws customException if value already exists in database
	 */
	public void checkExist(String toverify,String tablename,String columnName,DataSource datasource)  
	{
		try{
			Connection conn=datasource.getConnection();
			/**********Checking if required Value already exists *********************/
			String checkvalue = "SELECT * from "+tablename+" "+"WHERE "+columnName+" =?";
			ps= conn.prepareStatement(checkvalue);
			ps.setString(1, toverify);
			rs = ps.executeQuery();//executing query
			if(rs.next())
				{
					throw new CustomException(505,"Duplicate entry : "+columnName+" "+"'"+toverify+"'"+"already exists");
				}
		}
		catch (SQLException e) {
			throw new CustomException(e.getErrorCode(),e.getMessage());
		}
	}
	
	/**
	 * @param toverify
	 * @param tablename
	 * @param columnName
	 * @param datasource
	 * @throws SQLException
	 * @throws customException if value does not exist in database
	 */
	public void checkNotExist(String toverify,String tablename,String columnName,DataSource datasource)
	{
		try{
			Connection conn=datasource.getConnection();
			/**********Checking if required Value does not exists *********************/
			String checkvalue = "SELECT * from "+tablename+" "+"WHERE "+columnName+" =?";
			ps= conn.prepareStatement(checkvalue);
			ps.setString(1, toverify);
			rs = ps.executeQuery();//executing query
			if(rs.next()){}
			else
				throw new CustomException(505,"No such "+columnName+" '"+toverify+"' exists");
			}
		catch (SQLException e) {
			throw new CustomException(e.getErrorCode(),e.getMessage());
		}
		
	}
	
	/**
	 * @param toverify1
	 * @param whereVerify
	 * @param tablename
	 * @param verifyColumn
	 * @param whereColumnName
	 * @param datasource
	 * @throws SQLException
	 * @throws customException if values does not map to same entity in database
	 */
	public void checkMapping(String toverify1,String whereVerify,String tablename,String verifyColumn,String whereColumnName,DataSource datasource) 
	{
		try{
			Connection conn=datasource.getConnection();
			/**********Checking if required both values map to same entity*********************/
			String checkvalue = "SELECT "+verifyColumn+" from "+tablename+" "+"WHERE "+whereColumnName+" =?";
			ps= conn.prepareStatement(checkvalue);
			ps.setString(1, whereVerify);
			rs = ps.executeQuery();//executing query
			if(rs.next()){
				if(toverify1.equals(rs.getString(1)))
				{}
				else
					throw new CustomException(505, verifyColumn+" and "+whereColumnName+" does not match");
			}
			else
				throw new CustomException(505, verifyColumn+" and "+whereColumnName+" does not match");
		}
		catch (SQLException e) {
			throw new CustomException(e.getErrorCode(),e.getMessage());
		}
	}
	
	public void checkNull(String toCheck,String toDisplay)
	{
		if(toCheck==null||toCheck.isEmpty())
		{
			throw new CustomException(505,toDisplay+" is empty or null");
		}
	}
}
