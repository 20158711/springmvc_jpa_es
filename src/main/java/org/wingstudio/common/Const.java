package org.wingstudio.common;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.management.loading.MLetContent;
import java.util.Set;

public class Const {
    public interface TAG {
        String CURRENT_USER = "current_user";
        String USERNAME = "username";
        String EMAIL = "email";
    }

    public interface ERROR_MSG {
        String USERNAME_NOT_EXISTS = "username not exists";
        String USERNAME_ALREADY_EXISTS = "username already exists";
        String EMAIL_NOT_EXISTS = "email not exists";
        String EMAIL_ALREADY_EXISTS = "email already exists";
        String PASSWORD_ERROR = "password error";
    }

    public interface ROLE {
        short ROLE_CUSTOMER = 0;
        short ROLE_ADMIN = 1;
    }

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC= Sets.newHashSet("price_desc","price_asc");
    }

    @Getter
    @AllArgsConstructor
    public enum ProductStatusEnum {
        ON_SELL(Byte.valueOf("1"), "在线"),
        ON_NOT_SELL(Byte.valueOf("0"), "下线");
        private Byte code;
        private String value;
    }

    public interface Cart{
        byte CHECKED=1;
        byte UN_CHECKED=0;
        String LIMIT_NUM_SUCCESS="success";
        String LIMIT_NUM_FAIL="fail";
    }

    @Getter
    @AllArgsConstructor
    public enum PaymentTypeEnum{
        ONLINE_PAY(Byte.valueOf("1"),"在线支付");
        private byte code;
        private String value;
        public static PaymentTypeEnum codeOf(byte code){
            for (PaymentTypeEnum anEnum : values()) {
                if (anEnum.getCode()==code){
                    return anEnum;
                }
            }
            throw new RuntimeException("没有这样的状态");
        }
    }

    @Getter
    @AllArgsConstructor
    public enum OrderStatusEnum{
        NO_PAY(Byte.valueOf("1"),"未付款"),
        CANCELED(Byte.valueOf("2"),"订单已经取消"),
        PAID(Byte.valueOf("3"),"订单已付款"),
        SHIPPED(Byte.valueOf("4"),"订单已发货");
        private Byte code;
        private String value;
        public static OrderStatusEnum codeOf(Byte code){
            for (OrderStatusEnum anEnum : values()) {
                if(anEnum.getCode()==code){
                    return anEnum;
                }
            }
            throw new RuntimeException("没有这样的状态");
        }
    }
}
