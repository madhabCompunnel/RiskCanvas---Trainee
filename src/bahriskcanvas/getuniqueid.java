/**
class for generating unique id's
*/
package bahriskcanvas;

import java.util.Random;//for generating random string for unique RoleID

public class getuniqueid {
	/**
	* method for generating unique Roleid
	 * @return id
	*/
	public String uniqueid()
	{
		char[] chars = "abcdefghijklmnopqrstuvwxyzABSDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
		Random r = new Random(System.currentTimeMillis());
		char[] id = new char[10];
		for (int i = 0;  i < 10;  i++) 
		{
		    id[i] = chars[r.nextInt(chars.length)];
		}
		return new String(id);
	}	

}
