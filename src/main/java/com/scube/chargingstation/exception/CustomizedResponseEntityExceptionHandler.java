package com.scube.chargingstation.exception;
import com.scube.chargingstation.dto.response.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    
    @ExceptionHandler(ArithmeticException.class)
	public final ResponseEntity handaleArithmeticException(Exception ex, WebRequest request){
		
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