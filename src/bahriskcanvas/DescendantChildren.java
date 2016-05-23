package bahriskcanvas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DescendantChildren 
{
	public void getChildren(ArrayList<String> children,int size,Connection con) throws Exception
	{
		for(int i=size;i<children.size();i++)
		{
			PreparedStatement ps=con.prepareStatement("select group_id from tbl_groups where parent_id=?");
			ps.setString(1, children.get(i));
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				System.out.println(rs.getString(1));
				children.add(rs.getString(1));
			}
			getChildren(children,children.size()-size,con);
		}
			
	}

}
