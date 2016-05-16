
package bahriskcanvas;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
public class User {

    private String UserName;
    private String Role;
    private String FName;
    private String LName;
    private String alfTicket;
    private String defaultScreen;
    private ArrayList<Permission> permissions = new ArrayList<Permission>();
    
    @JsonProperty("UserName")
    public String getUserName() 
    {
        return UserName;
    }
  
    public void setUserName(String UserName) 
    {
        this.UserName = UserName;
    }
    @JsonProperty("Role")
    public String getRole() 
    {
        return Role;
    }
    
    public void setRole(String Role) 
    {	
        this.Role = Role;
    }
    @JsonProperty("FName")
    public String getFName() 
    {
        return FName;
    }
 
    public void setFName(String FName)
    {
        this.FName = FName;
    }
    @JsonProperty("LName")
    public String getLName() 
    {
        return LName;
    }

    public void setLName(String LName) 
    {
        this.LName = LName;
    }
    @JsonProperty("alf_ticket")
    public String getAlfTicket() 
    {
        return alfTicket;
    }
    
    public void setAlfTicket(String alfTicket) 
    {
        this.alfTicket = alfTicket;
    }
    @JsonProperty("defaultScreen")
    public String getDefaultScreen() 
    {
        return defaultScreen;
    }

    public void setDefaultScreen(String defaultScreen) 
    {
        this.defaultScreen = defaultScreen;
    }
    @JsonProperty("permissions")
    public List<Permission> getPermissions() 
    {
        return permissions;
    }

    public void setPermissions(ArrayList<Permission> permissions) 
    {
        this.permissions = permissions;
    }

}
