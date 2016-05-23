package riskcanvas.exceptionhandler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;

import bahriskcanvas.ErrorResponse;
import bahriskcanvas.UserException;
import bahriskcanvas.customException;
import bahriskcanvas.throwException;

@ControllerAdvice
public class RestExceptionHandler {
	
	@ExceptionHandler(customException.class)
	@ResponseBody
	public throwException handleCustomException(customException ex) 
	{
		throwException exception=new throwException();
		exception.setSuccess(false);
		exception.setErrcode(ex.getErrcode());
		exception.setErrmsg(ex.getErrmsg());
		return exception;
	}
	
	@ExceptionHandler(UserException.class)
	public ErrorResponse exceptionHandler(Exception ex) 
	{
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(HttpStatus.BAD_REQUEST);
		error.setMessage(ex.getMessage());
		return error;
	}

}
