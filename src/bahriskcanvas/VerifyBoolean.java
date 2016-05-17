package bahriskcanvas;

public class VerifyBoolean {
	
	public void isboolean(CreateRoleInput roleinput)
	{
		int i,j;
		String isActive=roleinput.getIsActive();
		if(isActive=="true"||isActive=="false")
			{}
		else
			throw new customException(400,"Invalid Data:Only boolean values allowed for feild "+"isActive");
		
		int menusize=roleinput.getMenulist().size();
		for(i=0;i<menusize;i++)
		{
		if((roleinput.getMenulist().get(i).getValue()=="true")||(roleinput.getMenulist().get(i).getValue()=="false"))
			{}
		else
			throw new customException(400,"Invalid Data:Only boolean values allowed for feild "+"\"value\""+"in menuList");
		}
		
		int permissionsize=roleinput.getMenulist().get(0).getPermissions().size();
		for(i=0;i<menusize;i++)
		for(j=0;j<permissionsize;j++)
		{
		if((roleinput.getMenulist().get(i).getPermissions().get(j).getValue()=="true")||(roleinput.getMenulist().get(i).getPermissions().get(j).getValue()=="false"))
			{}
		else
			throw new customException(400,"Invalid Data:Only boolean values allowed for feild "+"\"value\""+"in permissions");
		}
	}

}
