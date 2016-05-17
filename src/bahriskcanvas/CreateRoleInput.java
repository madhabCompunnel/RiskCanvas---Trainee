//model for getting json input for new role creation
package bahriskcanvas;

import java.util.ArrayList;
import java.util.List;

public class CreateRoleInput {
	private String roleId;
	private String roleName;
	private String isActive;
	private String roleType;
	private String defaultScreen;
	private String assignedUser;
	private String alf_ticket;
	private List<Menulist> menulist = new ArrayList<Menulist>();
	
	/**
	* @return
	* The roleName
	*/
	public String getRoleName() {
	return roleName;
	}
	/**
	* @param roleName
	*/
	public void setRoleName(String roleName) {
	this.roleName = roleName;
	}
	/**
	* @return
	* The isActive
	*/
	public String getIsActive() {
	return isActive;
	}
	/**
	* @param isActive
	*/
	public void setIsActive(String isActive) {
	this.isActive = isActive;
	}
	/**
	* @return
	* The roleType
	*/
	public String getRoleType() {
	return roleType;
	}
	/**
	* @param roleType
	*/
	public void setRoleType(String roleType) {
	this.roleType = roleType;
	}
	/**
	* @return
	* The defaultScreen
	*/
	public String getDefaultScreen() {
	return defaultScreen;
	}
	/**
	* @param defaultScreen
	*/
	public void setDefaultScreen(String defaultScreen) {
	this.defaultScreen = defaultScreen;
	}
	/**
	* @return
	* The menulist
	*/
	public List<Menulist> getMenulist() {
	return menulist;
	}
	/** 
	 * @param menulist
	 */
	public void setMenulist(List<Menulist> menulist) {
	this.menulist = menulist;
	}
	/**
	* @return
	* The roleId
	*/
	public String getRoleId() {
		return roleId;
	}
	/**
	* @param roleId
	*/
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	/** 
	* @return
	* The assignedUser
	*/
	public String getAssignedUser() {
		return assignedUser;
	}
	/**
	* @param assignedUser
	*/
	public void setAssignedUser(String assignedUser) {
		this.assignedUser = assignedUser;
	}
	/**
	 * @return
	 * The alf_ticket
	 */
	public String getAlf_ticket() {
		return alf_ticket;
	}
	/**
	 * 
	 * @param alf_ticket
	 */
	public void setAlf_ticket(String alf_ticket) {
		this.alf_ticket = alf_ticket;
	}
}
