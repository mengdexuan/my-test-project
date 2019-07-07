package com.itangquan.datagenerate.biz.index.controller;

import com.itangquan.datagenerate.biz.camel.entity.CamelRoute;
import com.itangquan.datagenerate.biz.camel.repository.CamelRouteRepository;
import com.itangquan.datagenerate.biz.dict.service.DictService;
import com.itangquan.datagenerate.biz.index.service.IndexService;
import com.itangquan.datagenerate.biz.schedule.entity.ScheduleJob;
import com.itangquan.datagenerate.biz.schedule.service.ScheduleJobService;
import com.itangquan.datagenerate.biz.urllimit.entity.UrlLimit;
import com.itangquan.datagenerate.biz.urllimit.repository.UrlLimitRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
public class IndexController {


	@Autowired
	ScheduleJobService scheduleJobService;

	@Autowired
	CamelContext camelContext;

	@Autowired
	DictService dictService;

	@Autowired
	IndexService indexService;

	@Autowired
	UrlLimitRepository urlLimitRepository;

	@Autowired
	CamelRouteRepository camelRouteRepository;


	@RequestMapping("/")
	public String index(Model model) {

		log.info("首页被访问...");

		model.addAttribute("sysTime", System.currentTimeMillis());

		try {
			Thread.sleep(11);
		} catch (InterruptedException e) {

		}

		return "index";
	}


	@GetMapping(value = "/exit")
	@ResponseBody
	public String exit() {

		log.info("系统退出...");

		System.exit(0);

		return "ok";
	}




	@GetMapping(value = "/jobList")
	public String list(Model model) {

		List<ScheduleJob> jobList = scheduleJobService.findAll();

		model.addAttribute("jobList",jobList);

		return "jobList";
	}


	@GetMapping(value = "/urlLimitList")
	public String urlLimitList(Model model) {

		Sort sort1 = new Sort(Sort.Direction.DESC, "urlLimit");
		Sort sort2 = new Sort(Sort.Direction.ASC, "reqUrl");

		Sort sort = sort1.and(sort2);

		List<UrlLimit> urlLimitList = urlLimitRepository.findAll(sort);

		model.addAttribute("urlLimitList",urlLimitList);

		return "urlLimitList";
	}


	@GetMapping(value = "/camelRouteList")
	public String camelRouteList(Model model) {

		Sort sort = new Sort(Sort.Direction.ASC, "routeId");

		List<CamelRoute> camelRouteList = camelRouteRepository.findAll(sort);

		model.addAttribute("camelRouteList",camelRouteList);

		return "camelRouteList";
	}




}



















