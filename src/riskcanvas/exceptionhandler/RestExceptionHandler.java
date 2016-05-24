package riskcanvas.exceptionhandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ControllerAdvice;
import riskcanvas.exception.*;


@ControllerAdvice
public class RestExceptionHandler {
	
	@ExceptionHandler(CustomException.class)
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
