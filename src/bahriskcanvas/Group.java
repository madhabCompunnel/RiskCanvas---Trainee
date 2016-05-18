package bahriskcanvas;

import java.util.ArrayList;

public class Group 
{
	private String group_name;
	private String parentId;
	private int userCount;
	private String groupId;
	private ArrayList<Group> subgroup=new ArrayList<Group>();
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public int getUserCount() {
		return userCount;
	}
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public ArrayList<Group> getSubgroup() {
		return subgroup;
	}
	public void setSubgroup(ArrayList<Group> subgroup) {
		this.subgroup = subgroup;
	}
}
