package com.itangquan.datagenerate.biz.index.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.google.common.collect.Lists;
import com.itangquan.datagenerate.base.util.HelpMe;
import com.itangquan.datagenerate.biz.camel.entity.CamelRoute;
import com.itangquan.datagenerate.biz.camel.repository.CamelRouteRepository;
import com.itangquan.datagenerate.biz.dict.service.DictService;
import com.itangquan.datagenerate.biz.index.dto.ConfigFileDto;
import com.itangquan.datagenerate.biz.index.service.IndexService;
import com.itangquan.datagenerate.biz.schedule.entity.ScheduleJob;
import com.itangquan.datagenerate.biz.schedule.service.ScheduleJobService;
import com.itangquan.datagenerate.biz.urllimit.entity.UrlLimit;
import com.itangquan.datagenerate.biz.urllimit.repository.UrlLimitRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.StandardServletEnvironment;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class IndexController {


	@Autowired
	Environment environment;

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




	@GetMapping(value = "/configFile")
	public String configFile(Model model) {

		List<ConfigFileDto> configFileList = configFileList();

		model.addAttribute("configFileList",configFileList);

		return "configFile";
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




	private List<ConfigFileDto> configFileList(){

		List<ConfigFileDto> dtoList = Lists.newArrayList();

		List<String> list = Lists.newArrayList();

		MutablePropertySources sources = ((StandardServletEnvironment) environment).getPropertySources();

		Iterator<PropertySource<?>> it = sources.iterator();

		while (it.hasNext()){
			PropertySource<?> item = it.next();
			String name = item.getName();

			if (name.startsWith("applicationConfig")){
				list.add(name);
			}
		}

		log.info("配置文件列表：{}",list);

		list = list.stream().map(item->{
			return HelpMe.removeFirstChar(HelpMe.removeLastChar(item.split("applicationConfig: ")[1]));
		}).collect(Collectors.toList());


		List<String> fileList = list.stream().filter(item -> {
			return item.startsWith("file");
		}).collect(Collectors.toList());


		List<String> classpathList = list.stream().filter(item -> {
			return item.startsWith("classpath");
		}).collect(Collectors.toList());


		fileList = fileList.stream().map(item->{
			return item.split("file:./")[1];
		}).collect(Collectors.toList());


		if (HelpMe.isNotNull(fileList)){
			for (String item:fileList){
				File file = new File(item);
				ConfigFileDto dto = new ConfigFileDto();
				dto.setName(file.getName());
				dto.setContent(FileUtil.readUtf8String(file));

				dtoList.add(dto);
			}
		}else {
			if (HelpMe.isNotNull(classpathList)){
				for (String item:classpathList){
					ConfigFileDto dto = new ConfigFileDto();
					dto.setName(item);
					dto.setContent(ResourceUtil.readUtf8Str(item));

					dtoList.add(dto);
				}
			}
		}

		return dtoList;
	}


}



















