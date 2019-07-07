package com.itangquan.datagenerate.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 系统配置
 * @author mengdexuan on 2019/4/20 17:01.
 */
@Component
@Slf4j
public class SysConfig {

	//是否支持把流转
	public static boolean HANDLE_SUPPORT = false;
	//散把是否生成GZH
	public static boolean HANDLE_GZH = false;
	//散捆是否生成GZH
	public static boolean BUNDLE_GZH = false;
	//清分的FSN存储路径
	public static String FSN_PATH = null;

	@Value("${support.handle}")
	private Boolean flag;

	@Value("${gzh.make.bundle:false}")
	private Boolean bundleMake;

	@Value("${gzh.make.handle:false}")
	private Boolean handleMake;

	@Value("${fsn.path}")
	private String fsnPath;

	@PostConstruct
	private void init(){
		HANDLE_SUPPORT = flag;
		log.info("support.handle 配置：{}",HANDLE_SUPPORT);

		FSN_PATH = fsnPath;
		log.info("fsn.path 配置：{}",FSN_PATH);

		BUNDLE_GZH = bundleMake;

		if (HANDLE_SUPPORT){
			HANDLE_GZH = handleMake;
		}

	}
}
