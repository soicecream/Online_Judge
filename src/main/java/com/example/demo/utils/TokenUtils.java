package com.example.demo.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.entity.User;
import com.example.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 生成token
 *
 * @return
 */

/**
 * 将TokenUtils注册位springboot的一个Bean
 * 注册完后 通过Resource把userService对象引入
 * 再通过PostConstruct 在程序运行时 把userService对象 赋值给静态的staticUserService对象
 * 赋值给静态的是方便下面的使用
 * <p>
 * 静态的方法不能使用非静态的方法
 */

@Component
public class TokenUtils {

    private static IUserService staticUserService;

    @Resource
    private IUserService userService;

    @PostConstruct
    public void setUserService() {
        staticUserService = userService;
    }

    //    形如***.***.***
    //    头部(header).载荷(payload).签证(signature)
    public static String getToken(String userId, String sign) {
        return JWT.create().withAudience(userId) // 将user id 保存到token里面，作为载荷
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) /// 2小时后token过期
                .sign(Algorithm.HMAC256(sign)); // 以password作为token得到密钥
    }


    /**
     * 获取当前登录的用户信息
     *
     * @return
     */
    public static User getCurrenUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserService.getById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
