
package bahriskcanvas;

import java.util.ArrayList;
import java.util.List;

public class MoveGroup {

    private String groupId;
    private String parentId;
    private String destinationGroupId;
    private List<Object> subGroup = new ArrayList<Object>();

    public String getGroupId() 
    {
        return groupId;
    }

    public void setGroupId(String groupId) 
    {
        this.groupId = groupId;
    }

    public String getParentId() 
    {
        return parentId;
    }

     public void setParentId(String parentId) 
     {
        this.parentId = parentId;
     }
    
     public String getDestinationGroupId() 
     {
        return destinationGroupId;
     }
    
    public void setDestinationGroupId(String destinationGroupId) 
    {
        this.destinationGroupId = destinationGroupId;
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
