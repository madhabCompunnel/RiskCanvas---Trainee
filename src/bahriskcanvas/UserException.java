package bahriskcanvas;
public class UserException extends Exception
{
	private static final long serialVersionUID = 1L;
	private String errorMessage;
	public UserException()
	{
		super();
	}
	private int errorCode;
	public String getErrorMessage() {
		return errorMessage;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public UserException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}

}
