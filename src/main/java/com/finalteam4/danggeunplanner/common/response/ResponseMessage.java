package com.finalteam4.danggeunplanner.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMessage<T> {
    private final ResponseEntity<?> responseEntity;
    private final String msg;
    private final T data;
    public ResponseMessage(ResponseEntity<?> responseEntity, String message, T data){
        this.responseEntity =responseEntity;
        this.msg = message;
        this.data = data;
    }
}
