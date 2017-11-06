package com.nchu.exception;

/**
 * 2017-9-25 14:26:01
 * 收货地址Service层异常
 */
public class ReceivingAddressException extends ServiceException {

    public ReceivingAddressException() {
    }


    public ReceivingAddressException(String message) {
        super(message);
    }
}
