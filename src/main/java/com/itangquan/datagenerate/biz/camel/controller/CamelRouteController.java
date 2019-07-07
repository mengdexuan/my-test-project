package com.itangquan.datagenerate.biz.camel.controller;


import com.itangquan.datagenerate.base.Result;
import com.itangquan.datagenerate.base.ResultUtil;
import com.itangquan.datagenerate.biz.camel.entity.CamelRoute;
import com.itangquan.datagenerate.biz.camel.service.CamelRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * camel路由 控制器
 * </p>
 *
 * @author code-generate-tool
 * @since 2019-06-27
 */
@RestController
@RequestMapping("/camel/camelRoute")
public class CamelRouteController {

	@Autowired
	private CamelRouteService camelRouteService;

	/**
	 * 获取camel路由列表
	 */
	@GetMapping(value = "/list")
	public Result<List<CamelRoute>> list() {
		return ResultUtil.buildSuccess(camelRouteService.findAll());
	}


	@PostMapping("/update")
	public Result update(String routeId) {

		camelRouteService.triggerRouteStatus(routeId);

		return ResultUtil.buildSuccess();
	}


}

