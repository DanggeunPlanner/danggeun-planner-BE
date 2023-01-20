package com.finalteam4.danggeunplanner.common.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

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
    public ResponseEntity<?> handleNotValidFormatException(MethodArgumentNotValidException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(ErrorCode.NOT_VALID_FORMAT.getCode(), Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage()));
    }

}
