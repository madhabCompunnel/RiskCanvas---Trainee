// class for catching custom exception and setting error code and error message accordingly
package bahriskcanvas;

public class customException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private int errcode;
	private String errmsg;	
	/** 
	 * @return 
	 * The errcode
	 */
	public int getErrcode() {
		return errcode;
	}
	/**
	 * @param errcode
	 */
	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}
	/**
	 * @return 
	 * The errmsg
	 */
	public String getErrmsg() {
		return errmsg;
	}
	/**
	 * @param errmsg
	 */
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	/**
	 * @param errcode
	 * @param errmsg
	 */
	public customException(int errcode,String errmsg)
	{
		this.errcode=errcode;
		this.errmsg=errmsg;
	}
}
