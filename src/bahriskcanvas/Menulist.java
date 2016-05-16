//class for menulist
package bahriskcanvas;

import java.util.ArrayList;
import java.util.List;

public class Menulist {
	private String id;
	private String description;
	private Boolean value;
	private List<RolePermission> permissions = new ArrayList<RolePermission>();
	/**
	* @return
	* The id
	*/
	public String getId() {
	return id;
	}
	/**
	* @param id
	* The id
	*/
	public void setId(String id) {
	this.id = id;
	}
	/**
	* @return
	* The description
	*/
	public String getDescription() {
	return description;
	}
	/**
	* @param description
	* The description
	*/
	public void setDescription(String description) {
	this.description = description;
	}
	/**
	* @return
	* The value
	*/
	public Boolean getValue() {
	return value;
	}
	/**
	* @param value
	* The value
	*/
	public void setValue(Boolean value) {
	this.value = value;
	}
	/**
	* @return
	* The permissions
	*/
	public List<RolePermission> getPermissions() {
	return permissions;
	}
	/**
	* @param permissions
	* The permissions
	*/
	public void setPermissions(List<RolePermission> permissions) {
	this.permissions = permissions;
	}
}
