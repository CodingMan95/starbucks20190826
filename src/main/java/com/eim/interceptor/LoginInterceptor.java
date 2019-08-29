package com.eim.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.eim.config.RedisConfig;
import com.eim.kit.ConstantKit;
import com.eim.util.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    //鉴权失败后返回的HTTP错误码，默认为401
    private int unauthorizedErrorCode = HttpServletResponse.SC_UNAUTHORIZED;

    /**
     * 存放登录用户模型Key的Request Key
     */
    public static final String REQUEST_CURRENT_KEY = "REQUEST_CURRENT_KEY";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getParameter("token");
        log.info("token is {}", token);
        String userName = "";
        Jedis jedis = new Jedis(PropertiesUtils.getCommonYml("spring.redis.host").toString(), Integer.valueOf(PropertiesUtils.getCommonYml("spring.redis.port").toString()));
        jedis.auth(PropertiesUtils.getCommonYml("spring.redis.password").toString());
        if (token != null && token.length() != 0) {
            userName = jedis.get(token);
            log.info("account is {}", userName);
        }
        if (userName != null && !userName.trim().equals("")) {
            Long tokeBirthTime = Long.valueOf(jedis.get(token + userName));
            log.info("token Birth time is: {}", tokeBirthTime);
            Long diff = System.currentTimeMillis() - tokeBirthTime;
            log.info("token is exist : {} ms", diff);
            if (diff > ConstantKit.TOKEN_RESET_TIME) {
                jedis.expire(userName, ConstantKit.TOKEN_EXPIRE_TIME);
                jedis.expire(token, ConstantKit.TOKEN_EXPIRE_TIME);
                log.info("Reset expire time success!");
                Long newBirthTime = System.currentTimeMillis();
                jedis.set(token + userName, newBirthTime.toString());
                jedis.expire(token + userName, ConstantKit.TOKEN_EXPIRE_TIME);
            }

            //用完关闭
            jedis.close();
            request.setAttribute(REQUEST_CURRENT_KEY, userName);
            return true;

        } else {
            JSONObject jsonObject = new JSONObject();

            PrintWriter out = null;
            try {
                response.setStatus(unauthorizedErrorCode);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                jsonObject.put("status", false);
                jsonObject.put("errorCode", response.getStatus());
                jsonObject.put("message", HttpStatus.UNAUTHORIZED);
                jsonObject.put("data", "用户未登录");
                out = response.getWriter();
                out.println(jsonObject);

                return false;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != out) {
                    out.flush();
                    out.close();
                }
            }
        }
        request.setAttribute(REQUEST_CURRENT_KEY, null);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

