package com.itangquan.datagenerate.biz.camel;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mengdexuan on 2019/6/28 11:57.
 */
@Slf4j
@Component
public class EchoBean {

	@Autowired
	CamelContext camelContext;

	@Handler
	public String echo(String echo) {
		log.info(echo);
		return echo + " " + echo;
	}

	public String bar(String bar) {
		return bar + " " + bar;
	}

}
