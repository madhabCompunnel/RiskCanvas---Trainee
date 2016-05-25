package riskcanvas.model;

public class EditGroup {

    private String groupId;
    private String groupName;
    private String parentId;
    private String destinationGroupId;
    /**
     * 
     * @return
     *     The groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * 
     * @param groupId
     *     The groupId
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * 
     * @return
     *     The groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * 
     * @param groupName
     *     The groupName
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    /**
     * 
     * @return
     * 	The parentId
     */
	public String getParentId() {
		return parentId;
	}
	
	/**
	 * 
	 * @param parentId
	 * 	The parentId
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	/**
	 * 
	 * @return
	 * The destinationGroupId
	 */
	public String getDestinationGroupId() {
		return destinationGroupId;
	}
	
	/**
	 * 
	 * @param destinatonGroupId
	 * 	The destinationGroupId
	 */
	public void setDestinationGroupId(String destinationGroupId) {
		this.destinationGroupId = destinationGroupId;
	}

}
