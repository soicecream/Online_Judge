package com.example.demo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口统一返回的包装类
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private String code; // 告诉前端是成功还是失败
    private String message; // 失败的原因
    private Object data; // 携带的数据


    //    返回成功
    public static Result success() {
        return new Result(Constants.CODE_200, "", null);
    }
    public static Result success(Object data) {
        return new Result(Constants.CODE_200, "", data);
    }

    //    错误
    public static Result error() {
        return new Result(Constants.CODE_500, "系统错误", null);
    }
    public static Result error(String code, String massage) {
        return new Result(code, massage, null);
    }

}
