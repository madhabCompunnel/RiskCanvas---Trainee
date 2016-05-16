
package bahriskcanvas;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class CreateGroup {

private String groupName;
private String isParent;
private String isChild;
private String selectedGroupName;
private List<Object> subGroup = new ArrayList<Object>();

/**
* 
* @return
* The groupName
*/
public String getGroupName() {
return groupName;
}

/**
* 
* @param groupName
* The groupName
*/
public void setGroupName(String groupName) {
this.groupName = groupName;
}

/**
* 
* @return
* The isParent
*/
public String getIsParent() {
return isParent;
}

/**
* 
* @param isParent
* The isParent
*/
public void setIsParent(String isParent) {
this.isParent = isParent;
}

/**
* 
* @return
* The isChild
*/
public String getIsChild() {
return isChild;
}

/**
* 
* @param isChild
* The isChild
*/
public void setIsChild(String isChild) {
this.isChild = isChild;
}

/**
* 
* @return
* The selectedGroupName
*/
public String getSelectedGroupName() {
return selectedGroupName;
}

/**
* 
* @param selectedGroupName
* The selectedGroupName
*/
public void setSelectedGroupName(String selectedGroupName) {
this.selectedGroupName = selectedGroupName;
}

/**
* 
* @return
* The subGroup
*/
public List<Object> getSubGroup() {
return subGroup;
}

/**
* 
* @param subGroup
* The subGroup
*/
public void setSubGroup(List<Object> subGroup) {
this.subGroup = subGroup;
}

}