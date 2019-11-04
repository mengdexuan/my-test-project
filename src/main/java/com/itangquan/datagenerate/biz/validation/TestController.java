package com.itangquan.datagenerate.biz.validation;

import com.itangquan.datagenerate.base.Result;
import com.itangquan.datagenerate.base.ResultUtil;
import com.itangquan.datagenerate.base.annotation.PrintTime;
import com.itangquan.datagenerate.base.util.HelpMe;
import com.itangquan.datagenerate.biz.webshell.SshServerInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.impl.EventDrivenConsumerRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author mengdexuan on 2019/6/19 18:28.
 */
@RestController
@Slf4j
@RequestMapping("/test")
public class TestController {

	@Autowired
	Executor executor;
	@Autowired
	RequestMappingHandlerMapping handlerMapping;


	@Autowired
	CamelContext camelContext;


	public String test(String param){

		String result = "";

		log.info("参数：{}",param);

		result = "你好："+param;

		return result;
	}


	@GetMapping("/test1")
	@ResponseBody
	public Result test1(HttpServletRequest request) throws Exception {

		Object obj = null;

		List<Route> routes = camelContext.getRoutes();

		routes.stream().forEach(item->{
			EventDrivenConsumerRoute route = ((EventDrivenConsumerRoute) item);
			log.info("路由：{}，参数：{}",item.getId(),route.getStatus());
		});

		return ResultUtil.buildSuccess(obj);
	}






	@GetMapping("/test2")
	@ResponseBody
	public Result test2(HttpServletRequest request) {

		Object obj = null;

		String flag = request.getParameter("flag");

		if ("1".equals(flag)){
			try {
				camelContext.stopRoute("route2");
			} catch (Exception e) {
				log.error("停止路由失败！",e);
			}
		}else {

			try {
				camelContext.startRoute("route2");
			} catch (Exception e) {
				log.error("启动路由失败！",e);
			}
		}





		return ResultUtil.buildSuccess(obj);
	}



	@GetMapping("/test3")
	@ResponseBody
	public Result test3() {
		Object obj = null;

		List<SshServerInfo> dataList = HelpMe.readFromFile(SshServerInfo.class, "log/test.json");

		obj = dataList;


		return ResultUtil.buildSuccess(obj);
	}





	@PrintTime
	@PostMapping("/test4")
	public Result<String> test4(@Validated ValidatedBean validatedBean){


		return ResultUtil.buildSuccess("abc");
	}



	@PrintTime
	@PostMapping("/test5")
	public Result test5(@Validated @RequestBody ValidatedBean validatedBean){



		return ResultUtil.buildSuccess();
	}






}
