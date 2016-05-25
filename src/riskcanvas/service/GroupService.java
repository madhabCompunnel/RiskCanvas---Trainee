package riskcanvas.service;

import javax.servlet.http.HttpServletRequest;
import riskcanvas.model.EditGroup;

/**
 * 
 * @author 13083
 *  Interface GroupService
 */
public interface GroupService 
{
	public boolean editGroup(String alfTicket,EditGroup editGroup,HttpServletRequest request);

}
