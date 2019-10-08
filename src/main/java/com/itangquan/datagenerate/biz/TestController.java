package com.itangquan.datagenerate.biz;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpUtil;
import com.google.common.collect.Lists;
import com.itangquan.datagenerate.base.Result;
import com.itangquan.datagenerate.base.ResultUtil;
import com.itangquan.datagenerate.base.util.HelpMe;
import com.itangquan.datagenerate.biz.webshell.SshServerInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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




	@GetMapping("/test1")
	@ResponseBody
	public Result test1(HttpServletRequest request) throws Exception {

		Object obj = null;

		List<SshServerInfo> dataList = Lists.newArrayList();


		SshServerInfo info = new SshServerInfo();
		info.setNote("abc");

		dataList.add(info);

		SshServerInfo info2 = new SshServerInfo();
		info2.setUsername("userName");
		info2.setNote("abc2");
		dataList.add(info2);


		HelpMe.write2File(dataList,"log/test.json",false);



		return ResultUtil.buildSuccess(obj);
	}


	public static void main(String[] args) {

		Executor executor = Executors.newFixedThreadPool(1);

		int count = 4;

		for (int i =0;i<count;i++){
			executor.execute(()->{
				String result = HttpUtil.get("http://localhost:1018/dataGenerate/test/test1");
				log.info(result);
			});
		}


		ThreadUtil.safeSleep(2000);
		((ExecutorService) executor).shutdown();
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




}
