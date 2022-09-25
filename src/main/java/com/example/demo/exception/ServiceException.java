package com.example.demo.exception;

import lombok.Getter;

/**
 * 自定义异常
 */

@Getter
public class ServiceException extends RuntimeException{

    private String code;

    public ServiceException(String code, String massage) {
        super(massage);
        this.code = code;
    }

}
