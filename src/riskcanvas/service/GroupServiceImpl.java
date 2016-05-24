package riskcanvas.service;
import javax.servlet.http.HttpServletRequest;
import riskcanvas.dao.group.GroupDao;
import riskcanvas.exception.CustomException;
import riskcanvas.model.EditGroup;

public class GroupServiceImpl implements GroupService 
{
	GroupDao groupDao;
	public void setGroupDao(GroupDao groupDao)
	{
		this.groupDao=groupDao;
	}
	@Override
	public boolean editGroup(String alfTicket,EditGroup editGroup,HttpServletRequest request)
	{
		if(alfTicket==null)
		{
			throw new CustomException(400,"Header does not contains ticket");
		}
		if(editGroup==null)
		{
			throw new CustomException(400,"check for group_Id and groupName,fields cannot be left empty");
		}
		boolean result=groupDao.editGroup(editGroup,request,alfTicket);
		return result;
	}

}
