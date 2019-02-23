package web.yyh.web.WuLiaoChat.push.service;

import web.yyh.web.WuLiaoChat.push.bean.db.User;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * @author Yyhyplxyz Email:xxw9808@163.com
 * @version 1.0.0
 */
public class BaseService {
    // 添加一个上下文注解，该注解会给securityContext赋值
    // 具体的值为我们的拦截器中所返回的SecurityContext
    @Context
    protected SecurityContext securityContext;


    /**
     * 从上下文中直接获取自己的信息
     *
     * @return User
     */
    protected User getSelf() {
        return (User) securityContext.getUserPrincipal();
    }
}
