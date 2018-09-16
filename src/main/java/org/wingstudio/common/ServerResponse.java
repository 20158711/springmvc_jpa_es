package org.wingstudio.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Getter
public class ServerResponse<T> {

    private int status;
    private String msg;
    private T data;

    private ServerResponse(){}

    private ServerResponse(Integer status) {
        this.status = status;
    }

    private ServerResponse(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServerResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess(){
        return status==ResponseCode.SUCCESS.getCode();
    }

    public static <T> ServerResponse<T> success(){ return new ServerResponse<>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> successMessage(String msg){
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServerResponse<T> success(T data){
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(),data);
    }

    public static <T> ServerResponse<T> success(String msg, T data){
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    public static <T> ServerResponse<T> error(){
        return new ServerResponse<>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> errorMessage(String error){
        return new ServerResponse<>(ResponseCode.ERROR.getCode(),error);
    }

    public static <T> ServerResponse<T> errorCodeMessage(int errorCode, String errorMsg){
        return new ServerResponse<>(errorCode,errorMsg);
    }

    public static <T> ServerResponse<T> errorCodeMessage(ResponseCode responseCode){
        return new ServerResponse<>(responseCode.getCode(),responseCode.getDesc());
    }
}
