//class for returning exception data to the user
package riskcanvas.exception;

public class ThrowException {
	private Boolean success;
	private int errcode;
	private String errmsg;
	
	/**
	 * @return
	 * The success
	 */
	public Boolean getSuccess() {
		return success;
	}
	/**
	 * @param success
	 */
	public void setSuccess(Boolean success) {
		this.success = success;
	}
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
	
	
}
