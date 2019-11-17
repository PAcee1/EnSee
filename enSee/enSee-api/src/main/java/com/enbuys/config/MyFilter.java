package com.enbuys.config;

import com.enbuys.utils.JsonResult;
import com.enbuys.utils.JsonUtils;
import com.enbuys.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class MyFilter implements HandlerInterceptor {

    @Autowired
    public RedisOperator redis;

    public static final String USER_REDIS_SESSION = "user-redis-session";

    /**
     * 拦截请求，判断是否登录
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 解决springboot拦截器无法自动注入问题
        if (null == redis){
            WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            redis = applicationContext.getBean(RedisOperator.class);
        }
        String userId = request.getHeader("userId");
        String userToken = request.getHeader("userToken");
        String isSearch = request.getHeader("isSearch");

        // 判断是否登录
        if(!StringUtils.isBlank(userId) && StringUtils.equals(isSearch,"true")){
            return true;
        }
        if(!StringUtils.isBlank(userId) && !StringUtils.isBlank(userToken)){
            // 判断会话是否过期
            String session = redis.get(USER_REDIS_SESSION + ":" + userId);
            if(StringUtils.isBlank(session)){
                returnErrorResponse(response,JsonResult.errorTokenMsg("用户状态过期，请重新登陆"));
                return false;
            }
            // 判断登陆状态是否一致
            if(!StringUtils.equals(session,userToken)){
                returnErrorResponse(response,JsonResult.errorTokenMsg("用户在其它地点登陆，请重新登录"));
                return false;
            }
        }else{
            returnErrorResponse(response,JsonResult.errorTokenMsg("用户未登录，请登录"));
            return false;
        }
        return true;
    }

    public void returnErrorResponse(HttpServletResponse response, JsonResult result)
            throws IOException {
        OutputStream out=null;
        try{
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            out = response.getOutputStream();
            out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            out.flush();
        } finally{
            if(out!=null){
                out.close();
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
