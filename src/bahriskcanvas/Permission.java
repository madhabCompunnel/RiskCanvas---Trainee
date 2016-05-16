
package bahriskcanvas;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Permission {

    private String id;
    private String description;
    private Boolean value;
    
    
    public Permission(String pid,String pdesc,boolean pval)
    {
    	id=pid;
    	description=pdesc;
    	value=pval;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    
    public String getDescription() {
        return description;
    }

   
    public void setDescription(String description) {
        this.description = description;
    }

    
    public Boolean getValue() {
        return value;
    }

    
    public void setValue(Boolean value) {
        this.value = value;
    }

}
