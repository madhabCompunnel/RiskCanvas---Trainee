package riskcanvas.dao.group;
import javax.servlet.http.HttpServletRequest;
import riskcanvas.model.EditGroup;

public interface GroupDao 
{
	boolean editGroup(EditGroup editGroup,HttpServletRequest request,String alfticket);

}
