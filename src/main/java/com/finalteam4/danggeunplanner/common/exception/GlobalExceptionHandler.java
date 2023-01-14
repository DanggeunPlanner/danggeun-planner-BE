package com.finalteam4.danggeunplanner.common.exception;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DanggeunPlannerException.class)
    public ResponseEntity<?> handleDanggeunPlannerException(DanggeunPlannerException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(new ErrorMessage(e.getErrorCode().getCode(), e.getErrorCode().getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleUnexpectedRuntimeException(){
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus()).body(new ErrorMessage(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleNotValidFormatException(){
        return ResponseEntity.status(ErrorCode.NOT_VALID_FORMAT.getHttpStatus()).body(new ErrorMessage(ErrorCode.NOT_VALID_FORMAT.getCode(), ErrorCode.NOT_VALID_FORMAT.getMessage()));
    }

}
