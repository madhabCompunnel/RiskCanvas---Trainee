//class for menuList
package bahriskcanvas;

import java.util.ArrayList;
import java.util.List;

public class Menulist {
	private String id;
	private String description;
	private String value;
	private List<RolePermission> permissions = new ArrayList<RolePermission>();
	
	public Menulist()
	{}
	
	/**
	 * @param id
	 * @param description
	 * @param value
	 */
	public Menulist(String id, String description, String value) {
		this.id=id;
		this.description=description;
		this.value=value;
	}
	
	public Menulist(String id, String description, String value,List<RolePermission> permissions) {
		this.id=id;
		this.description=description;
		this.value=value;
		this.permissions=permissions;
	}
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
	public String getValue() {
	return value;
	}
	/**
	* @param value
	* The value
	*/
	public void setValue(String value) {
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