package com.itangquan.datagenerate.biz.schedule.util;

import com.itangquan.datagenerate.base.util.HelpMe;
import com.itangquan.datagenerate.base.util.SpringContextUtil;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;


public class ScheduleRunnable implements Runnable {
    private Object target;
    private Method method;
    private String params;

    public ScheduleRunnable(String beanName, String methodName, String params) throws NoSuchMethodException, SecurityException {
        this.target = SpringContextUtil.getBean(beanName);
        this.params = params;
        if (HelpMe.isNotNull(params)) {
            this.method = target.getClass().getDeclaredMethod(methodName, String.class);
        } else {
            this.method = target.getClass().getDeclaredMethod(methodName);
        }
    }

    @Override
    public void run() {
        try {
            ReflectionUtils.makeAccessible(method);
            if (HelpMe.isNotNull(params)) {
                method.invoke(target, params);
            } else {
                method.invoke(target);
            }
        } catch (Exception e) {
            throw new RuntimeException("执行定时任务失败", e);
        }
    }

}
