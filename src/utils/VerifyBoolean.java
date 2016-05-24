//class for verifying boolean values
package utils;

import riskcanvas.exception.CustomException;
import riskcanvas.model.AddUser;

public class VerifyBoolean {
	/** 
	 * @param adduser
	 * @throws customException if value to be checked is not boolean
	 */
	public void isboolean(AddUser adduser)
	{
		int i,j;
		String isActive=adduser.getIsActive();
		if(isActive=="true"||isActive=="false")
			{}
		else
			throw new CustomException(400,"Invalid Data:Only boolean values allowed for feild "+"isActive");
		
		int menusize=adduser.getMenulist().size();
		for(i=0;i<menusize;i++)
		{
		if((adduser.getMenulist().get(i).getValue()=="true")||(adduser.getMenulist().get(i).getValue()=="false"))
			{}
		else
			throw new CustomException(400,"Invalid Data:Only boolean values allowed for feild value in menuList");
		}
			
		for(i=0;i<menusize;i++)
		{
			int permissionsize=adduser.getMenulist().get(i).getPermissions().size();
			for(j=0;j<permissionsize;j++)
			{
				if((adduser.getMenulist().get(i).getPermissions().get(j).getOverriddenValue()=="true")||(adduser.getMenulist().get(i).getPermissions().get(j).getOverriddenValue()=="false"))
				{}
				else
					throw new CustomException(400,"Invalid Data:Only boolean values allowed for feild overriddenValue in permissions");
			}
		}
	}

	
}
