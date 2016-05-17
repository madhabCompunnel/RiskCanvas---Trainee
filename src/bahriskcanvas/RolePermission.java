/**
 * Class for getting and setting role based permissions
 */
package bahriskcanvas;

public class RolePermission {
	private String id;
	private String description;
	private String value;

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
}
