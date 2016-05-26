//class for verifying boolean values
package bahriskcanvas;

public class VerifyBoolean {
	
	public void isboolean(CreateRoleInput roleinput)
	{
		int i,j;
		String isActive=roleinput.getIsActive();
		System.out.println("IsActive="+isActive);
		if(isActive.equals("true")||isActive.equals("false"))
			{}
		else
			throw new customException(400,"Invalid Data:Only boolean values allowed for feild isActive");
		
		int menusize=roleinput.getMenulist().size();
		for(i=0;i<menusize;i++)
		{
		if((roleinput.getMenulist().get(i).getValue().equals("true"))||(roleinput.getMenulist().get(i).getValue().equals("false")))
			{}
		else
			throw new customException(400,"Invalid Data:Only boolean values allowed for feild value in menuList");
		}
		
		
		for(i=0;i<menusize;i++)
		{
			int permissionsize=roleinput.getMenulist().get(i).getPermissions().size();
			for(j=0;j<permissionsize;j++)
			{
					if((roleinput.getMenulist().get(i).getPermissions().get(j).getValue().equals("true"))||(roleinput.getMenulist().get(i).getPermissions().get(j).getValue().equals("false")))
						{}
					else
						throw new customException(400,"Invalid Data:Only boolean values allowed for feild value in permissions");
			}
		}
	}
	
}
