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
	public void checkExist(String toverify,String tablename,String columnName,DataSource datasource) throws SQLException 
	{
		
		Connection conn=datasource.getConnection();
		/**********Checking if required Value already exists for creating and editing user*********************/
		String checkvalue = "SELECT * from "+tablename+" "+"WHERE "+columnName+" =?";
		ps= conn.prepareStatement(checkvalue);
		ps.setString(1, toverify);
		rs = ps.executeQuery();//executing query
		if(rs.next())
			{
				throw new CustomException(505,"Duplicate entry : "+columnName+" "+"'"+toverify+"'"+"already exists");
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
	public void checkNotExist(String toverify,String tablename,String columnName,DataSource datasource) throws SQLException
	{
		
		Connection conn=datasource.getConnection();
		/**********Checking if required Value already exists for creating and editing user*********************/
		String checkvalue = "SELECT * from "+tablename+" "+"WHERE "+columnName+" =?";
		ps= conn.prepareStatement(checkvalue);
		ps.setString(1, toverify);
		rs = ps.executeQuery();//executing query
		if(rs.next()){}
		else
			throw new CustomException(505,"No such "+columnName+" '"+toverify+"' exists");
	}

}
