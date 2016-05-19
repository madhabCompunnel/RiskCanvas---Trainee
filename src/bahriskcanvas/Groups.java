package bahriskcanvas;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Groups 
{
	private ArrayList<Group> groups=new ArrayList<Group>();

	@JsonProperty("groups")
	public ArrayList<Group> getGroup() {
		return groups;
	}

	public void setGroup(ArrayList<Group> group) {
		this.groups = group;
	}

}
