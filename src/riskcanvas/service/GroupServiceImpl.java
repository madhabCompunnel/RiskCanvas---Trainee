package riskcanvas.service;
import javax.servlet.http.HttpServletRequest;
import riskcanvas.dao.group.GroupDao;
import riskcanvas.exception.UserException;
import riskcanvas.model.EditGroup;

public class GroupServiceImpl implements GroupService 
{
	GroupDao groupDao;
	@Override
	public boolean editGroup(String alfTicket,EditGroup editGroup,HttpServletRequest request)
	{
		if(alfTicket==null)
		{
			throw new UserException("Header does not contains ticket");
		}
		if((editGroup.getGroupId()==null||editGroup.getGroupId().isEmpty())&& editGroup.getGroupName()==null||editGroup.getGroupName().isEmpty())
		{
			throw new UserException("check for group_Id and groupName,fields cannot be left empty");
		}
		
		groupDao.editGroup(editGroup,request,alfTicket);
		return false;
	}

}
