package com.finalteam4.danggeunplanner.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMessage<T> {
    private final String msg;
    private final T data;
    public ResponseMessage(String message, T data){
        this.msg = message;
        this.data = data;
    }
}
