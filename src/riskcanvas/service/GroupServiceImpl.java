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
		boolean result=false;
		try
		{
			if(!editGroup.getGroupId().isEmpty())
				{
				if(editGroup.getDestinationGroupId() == null)
					{
						result=groupDao.editGroup(editGroup,request,alfTicket);
					}
				else
					{
						result=groupDao.moveGroup(editGroup,request,alfTicket);
					}
					return result;
				}
				else
					{
						throw new CustomException(400,"check for group_id");
					}
		}
		catch(NullPointerException e)
		{
			throw new CustomException(400,"Invalid Input");
		}
	}
}
