package bahriskcanvas;
import java.util.ArrayList;
import java.util.List;

/*
 *  This class act as model for the Input JSON to create group.
 */
public class CreateGroup 
{

	private String groupName;
	private String isParent;
	private String isChild;
	private String selectedGroupName;
	private List<Object> subGroup = new ArrayList<Object>();

	public String getGroupName() 
	{
	return groupName;
	}
	public void setGroupName(String groupName)
	{
	this.groupName = groupName;
	}
	public String getIsParent() 
	{
	return isParent;
	}
	public void setIsParent(String isParent)
	{
	this.isParent = isParent;
	}
	public String getIsChild() 
	{
	return isChild;
	}
	public void setIsChild(String isChild) 
	{
	this.isChild = isChild;
	}
	public String getSelectedGroupName() 
	{
	return selectedGroupName;
	}
	public void setSelectedGroupName(String selectedGroupName)
	{
	this.selectedGroupName = selectedGroupName;
	}
	public List<Object> getSubGroup() 
	{
	return subGroup;
	}
	public void setSubGroup(List<Object> subGroup) 
	{
	this.subGroup = subGroup;
	}
}