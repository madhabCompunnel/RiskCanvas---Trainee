package bahriskcanvas;

import javax.sql.DataSource;

public class DatabaseConnection {
	private DataSource datasource;//Spring's own class for setting database related configuration in Beans.xml file
	
	/** 
	* @param Datasource
	* The Datasource
	*/
	public void setDatasource(DataSource datasource)
	{
		this.datasource = datasource;
	}
	/**
	 * @return
	 * The Datasource
	 */
	public DataSource getDatasource() {
		return datasource;
	}
}
