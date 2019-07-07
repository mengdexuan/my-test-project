package com.itangquan.datagenerate.config;

import com.itangquan.datagenerate.biz.urllimit.UrlLimitInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author mengdexuan on 2019/6/23 16:18.
 */
@Slf4j
@Configuration
public class WebMvc  implements WebMvcConfigurer {

	@Autowired
	UrlLimitInterceptor urlLimitInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(urlLimitInterceptor).addPathPatterns("/**");
	}

}
