package utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * CORS interceptor
 * 
 * @author nitin
 *
 */
@Component
public class CORSInterceptor extends HandlerInterceptorAdapter {



	private String allowedOrigins;
	private String allowedMethods;
	private String allowedHeaders;
	private String exposedHeaders;
	private String allowCredentials;
	private String maxAge;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception 
	{

		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
				allowedOrigins);
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
				allowedMethods);
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
				allowedHeaders);
		response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
				exposedHeaders);
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS,
				allowCredentials);
		response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, maxAge);

		return true;
	}

	public void setAllowedOrigins(String allowedOrigins) {
		this.allowedOrigins = allowedOrigins;
	}

	public void setAllowedMethods(String allowedMethods) {
		this.allowedMethods = allowedMethods;
	}

	public void setAllowedHeaders(String allowedHeaders) {
		this.allowedHeaders = allowedHeaders;
	}

	public void setExposedHeaders(String exposedHeaders) {
		this.exposedHeaders = exposedHeaders;
	}

	public void setAllowCredentials(String allowCredentials) {
		this.allowCredentials = allowCredentials;
	}

	public void setMaxAge(String maxAge) {
		this.maxAge = maxAge;
	}
}
