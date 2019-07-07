package com.itangquan.datagenerate.biz.camel;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mengdexuan on 2019/6/27 17:43.
 */
@Slf4j
@Component
public class CamelTimerRouteTest extends RouteBuilder {


	@Autowired
	CamelContext camelContext;

	@Override
	public void configure() throws Exception {

		from("timer://foo?period=2s").autoStartup(false).process(new Processor() {
			public void process(Exchange exchange) throws Exception {
				log.info("Hello world  :" + DateUtil.now());
			}
		});

		from("timer://myTimer1?period=5s").autoStartup(false)
				.routeDescription("这是测试").setBody().simple("Current time is ${header.firedTime}")
				.to("log:CamelTimerServer?showExchangeId=true");

		from("timer://myTimer2?period=3s").autoStartup(false).bean(EchoBean.class);

	}

}
