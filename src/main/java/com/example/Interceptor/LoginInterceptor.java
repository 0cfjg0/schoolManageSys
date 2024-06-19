package com.example.Interceptor;


import com.alibaba.fastjson.JSONObject;
import com.example.entity.R;
import com.example.utils.JWTUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {//过滤前
        String url = httpServletRequest.getRequestURI();
        System.out.println(url);
        if(url.matches(".?login.?") || url.matches(".?regist.?") || url.matches(".+job.+")){
            System.out.println("interceptor running");
            return true;//放行
        }else{
            String token = httpServletRequest.getHeader("token");
            if(token == null || token.equals("")){
                httpServletResponse.getWriter().write(JSONObject.toJSONString(R.error("NOT_LOGIN")));
                System.out.println("interceptor running");
                return false;//拦截
            }
            try{
                JWTUtils.parseJWT(token);
                System.out.println("interceptor running");
                return true;
            }catch (Exception e){
//                filterChain.doFilter(httpServletRequest,httpServletResponse);
                httpServletResponse.getWriter().write(JSONObject.toJSONString(R.error("NOT_LOGIN")));
                System.out.println("interceptor running");
                return false;
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {//过滤后
        System.out.println("方法执行结束");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {//请求结束
        System.out.println("请求结束");
    }
}