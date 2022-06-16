package com.scube.chargingstation.exception;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.dto.response.Response.Status;

import javax.naming.AuthenticationException;

import org.hibernate.JDBCException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by Keshav Patel.
 */
@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(CustomizedResponseEntityExceptionHandler.class);
	
    @ExceptionHandler(BRSException.EntityNotFoundException.class)
    public final ResponseEntity handleNotFountExceptions(Exception ex, WebRequest request) {
        Response response = Response.notFound();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BRSException.DuplicateEntityException.class)
    public final ResponseEntity handleNotFountExceptions1(Exception ex, WebRequest request) {
        Response response = Response.duplicateEntity();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity(response, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(BRSException.DataIntegrityViolationException.class)
    public final ResponseEntity handleConstraintViolationException(Exception ex, WebRequest request) {
        Response response = Response.duplicateEntity();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(BRSException.ConstraintViolationException.class)
    public final ResponseEntity handleDataIntegrityViolationException(Exception ex, WebRequest request) {
        Response response = Response.duplicateEntity();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ArithmeticException.class)
	public final ResponseEntity handaleArithmeticException(Exception ex, WebRequest request){
		
    	 Response response = Response.exception();
         response.addErrorMsgToResponse(ex.getMessage(), ex);
         logger.error(ex.getMessage(), ex);
         
         return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
	}
    
    @ExceptionHandler(BRSException.UnauthorizedEntityException.class)
   	public final ResponseEntity handaleUnauthorizedException(Exception ex, WebRequest request){
       	 Response response = Response.unauthorized();
            response.addErrorMsgToResponse(ex.getMessage(), ex);
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
   	}
    
    
    @ExceptionHandler(AuthenticationException.class)
	public final ResponseEntity handaleAuthenticationException(Exception ex, WebRequest request){
    	 Response response = Response.unauthorized();
         response.addErrorMsgToResponse(ex.getMessage(), ex);
         logger.error(ex.getMessage(), ex);
         
         return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
	}
    
    @ExceptionHandler(BadCredentialsException.class)
	public final ResponseEntity handaleBadCredentialsException(Exception ex, WebRequest request){
    	 Response response = Response.unauthorized();
         response.addErrorMsgToResponse(ex.getMessage(), ex);
         logger.error(ex.getMessage(), ex);
         
         return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
	}
    
    @ExceptionHandler(ConstraintViolationException.class)
   	public final ResponseEntity handaleConstraintViolationException(ConstraintViolationException ex, WebRequest request){
   		
       	 Response response = Response.exception();
            response.addErrorMsgToResponse(ex.getMessage(), ex);
            logger.error(ex.getMessage(), ex);
            
            return new ResponseEntity(response, HttpStatus.BAD_GATEWAY);
   	}
    
    @ExceptionHandler(DataIntegrityViolationException.class)
   	public final ResponseEntity handaleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request){
   		
       	 Response response = Response.exception();
            response.addErrorMsgToResponse(ex.getMessage(), ex);
            logger.error(ex.getMessage(), ex);
            
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
   	}
    
    @ExceptionHandler(Exception.class)
	public final ResponseEntity handaleException(Exception ex, WebRequest request){
		
    	 Response response = Response.exception();
         response.addErrorMsgToResponse(ex.getMessage(), ex);
         logger.error(ex.getMessage(), ex);
         
         return new ResponseEntity(response, HttpStatus.BAD_GATEWAY);
	}
    
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//	public final ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request){
//		
//    	 Response response = Response.exception();
//         response.addErrorMsgToResponse(ex.getMessage(), ex);
//         logger.error(ex.getMessage(), ex);
//         
//         return new ResponseEntity(response, HttpStatus.BAD_GATEWAY);
//	}
    
    
    
}