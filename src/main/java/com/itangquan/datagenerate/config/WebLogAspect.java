package com.itangquan.datagenerate.config;

import cn.hutool.json.JSONUtil;
import com.itangquan.datagenerate.base.util.HelpMe;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class WebLogAspect {

    //匹配所有的controller中的所有方法
    @Pointcut("execution(* com..controller.*.*(..))")
    public void printLog(){}

    @Before("printLog()")
    public void printLog(JoinPoint joinPoint) throws Throwable {
        requestBody(joinPoint);
    }

    /**
     * 请求参数拼装
     *
     * @param paramsArray
     * @return
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            params += JSONUtil.toJsonStr(paramsArray[0]) + " ";
        }
        return params.trim();
    }

    private void requestBody(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();


        // 记录下请求内容
        log.info("****** {} 请求， url: {}  ******",request.getMethod(),request.getRequestURL().toString());

        String param = "";
        if(HttpMethod.POST.name().equals(request.getMethod())) {
            param = argsArrayToString(joinPoint.getArgs());
        }else {
            param = JSONUtil.toJsonStr(request.getParameterMap());
        }

        if (HelpMe.isNull(param)){
            return;
        }
        if ("{}".equals(param)){
            return;
        }else {
            //请求参数不为空的时候打印日志
            log.info("请求参数：{}",param);
        }
    }

}
