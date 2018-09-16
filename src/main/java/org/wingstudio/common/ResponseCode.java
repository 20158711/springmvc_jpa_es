package org.wingstudio.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {
    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    NEED_LOGIN(10,"用户未登录，请登录"),
    NEED_ADMIN(11,"管理员才能操作"),
    ILLEGAL_ARGUMENT(2,"参数错误");
    private final int code;
    private final String desc;
}
