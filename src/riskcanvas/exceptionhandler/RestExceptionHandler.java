//ExceptionHandler for handling exceptions project-wide
package riskcanvas.exceptionhandler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;

import riskcanvas.exception.CustomException;
import riskcanvas.exception.ThrowException;

@ControllerAdvice
public class RestExceptionHandler{
	/**
	 * @param ex
	 * @return exception
	 */
	@ExceptionHandler({ CustomException.class })
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	@ResponseBody
	public ThrowException handleCustomException(CustomException ex) 
	{
		ThrowException exception=new ThrowException();
		exception.setSuccess(false);
		exception.setErrcode(ex.getErrcode());
		exception.setErrmsg(ex.getErrmsg());
		return exception;
	}

}
