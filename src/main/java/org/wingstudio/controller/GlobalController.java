package org.wingstudio.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wingstudio.common.ServerResponse;


@ControllerAdvice
@ResponseBody
public class GlobalController {

    @ExceptionHandler(Exception.class)
    public ServerResponse handleException(Exception e){
        return ServerResponse.errorMessage(e.getMessage());
    }

}
