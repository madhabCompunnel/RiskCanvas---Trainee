//model class for creating and editing User
package riskcanvas.model;

import java.util.ArrayList;
import java.util.List;
import bahriskcanvas.Group;
import bahriskcanvas.Menulist;

public class AddUser {

private String userName;
private String firstname;
private String lastname;
private String phonenumber;
private String email;
private String roleId;
private List<Group> group = new ArrayList<Group>();
private String isActive;
private String defaultScreen;
private String alf_ticket;
private List<Menulist> menulist = new ArrayList<Menulist>();

/**
* @return
* The userName
*/
public String getUserName() {
return userName;
}

/**
* 
* @param userName
* The userName
*/
public void setUserName(String userName) {
this.userName = userName;
}

/**
* 
* @return
* The firstname
*/
public String getFirstname() {
return firstname;
}

/**
* 
* @param firstname
* The firstname
*/
public void setFirstname(String firstname) {
this.firstname = firstname;
}

/**
* 
* @return
* The lastname
*/
public String getLastname() {
return lastname;
}

/**
* 
* @param lastname
* The lastname
*/
public void setLastname(String lastname) {
this.lastname = lastname;
}

/**
* 
* @return
* The phonenumber
*/
public String getPhonenumber() {
return phonenumber;
}

/**
* 
* @param phonenumber
* The phonenumber
*/
public void setPhonenumber(String phonenumber) {
this.phonenumber = phonenumber;
}

/**
* 
* @return
* The email
*/
public String getEmail() {
return email;
}

/**
* 
* @param email
* The email
*/
public void setEmail(String email) {
this.email = email;
}

/**
* 
* @return
* The roleId
*/
public String getRoleId() {
return roleId;
}

/**
* 
* @param roleId
* The roleId
*/
public void setRoleId(String roleId) {
this.roleId = roleId;
}

/**
* 
* @return
* The group
*/
public List<Group> getGroup() {
return group;
}

/**
* 
* @param group
* The group
*/
public void setGroup(List<Group> group) {
this.group = group;
}

/**
* 
* @return
* The isActive
*/
public String getIsActive() {
return isActive;
}

/**
* 
* @param isActive
* The isActive
*/
public void setIsActive(String isActive) {
this.isActive = isActive;
}

/**
* 
* @return
* The defaultScreen
*/
public String getDefaultScreen() {
return defaultScreen;
}

/**
* 
* @param defaultScreen
* The defaultScreen
*/
public void setDefaultScreen(String defaultScreen) {
this.defaultScreen = defaultScreen;
}
/**
 * @return
 * The alf_ticket
 */
public String getAlf_ticket() {
	return alf_ticket;
}
/**
 * @param alf_ticket
 */
public void setAlf_ticket(String alf_ticket) {
	this.alf_ticket = alf_ticket;
}

/**
* 
* @return
* The menuList
*/
public List<Menulist> getMenulist() {
return menulist;
}

/**
* 
* @param menulist
* The menuList
*/
public void setMenulist(List<Menulist> menulist) {
this.menulist = menulist;
}


}
