package bahriskcanvas;

public class Group 
{
	private String group_id;
	private String group_name;
	private String parent_id;
	
	Group(String group_id,String group_name,String parent_id)
	{
		this.group_id=group_id;
		this.group_name=group_name;
		this.parent_id=parent_id;
	}
	public String getGroup_id() 
	{
		return group_id;
	}
	public String getGroup_name() 
	{
		return group_name;
	}
	public String getParent_id() 
	{
		return parent_id;
	}
}
