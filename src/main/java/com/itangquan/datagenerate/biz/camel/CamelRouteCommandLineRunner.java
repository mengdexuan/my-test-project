package com.itangquan.datagenerate.biz.camel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itangquan.datagenerate.base.util.HelpMe;
import com.itangquan.datagenerate.biz.camel.entity.CamelRoute;
import com.itangquan.datagenerate.biz.camel.repository.CamelRouteRepository;
import com.itangquan.datagenerate.biz.camel.service.CamelRouteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.ServiceStatus;
import org.apache.camel.impl.EventDrivenConsumerRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author mengdexuan on 2019/6/23 12:21.
 */
@Order(2)
@Slf4j
@Component
public class CamelRouteCommandLineRunner implements CommandLineRunner{

	@Autowired
	CamelContext camelContext;
	@Autowired
	CamelRouteService camelRouteService;
	@Autowired
	CamelRouteRepository camelRouteRepository;
	@Autowired
	RouteUtil routeUtil;

	@Override
	public void run(String... args) throws Exception {

		List<CamelRoute> dbList = camelRouteService.findAll();

		List<Route> memoryList = camelContext.getRoutes();

		if (HelpMe.isNotNull(memoryList)&&HelpMe.isNull(dbList)){

			List<CamelRoute> camelRouteList = Lists.newArrayList();

			memoryList.stream().forEach(item->{
				CamelRoute camelRoute = trans2CamelRoute(item);
				camelRouteList.add(camelRoute);
			});

			camelRouteService.save(camelRouteList);
			return;
		}

		if (HelpMe.isNull(memoryList)){
			camelRouteRepository.deleteAll();
			return;
		}

		if (HelpMe.isNotNull(memoryList)){
			memoryList.stream().map(item->{
				return item.getId();
			}).collect(Collectors.toList());

			List<String> memoryIdList = memoryList.stream().map(item -> {
				return item.getId();
			}).collect(Collectors.toList());

			List<String> dbIdList = dbList.stream().map(item -> {
				return item.getRouteId();
			}).collect(Collectors.toList());

			List<String> diff = (List)CollUtil.disjunction(memoryIdList, dbIdList);

			if (HelpMe.isNull(diff)){
				//db 与 memory 数据一致
			}else {
				for (String item:dbIdList){
					if (!memoryIdList.contains(item)){
						//memory 中不存在，db 中存在，需要从 db 中删除
						CamelRoute camelRoute = new CamelRoute();
						camelRoute.setRouteId(item);
						camelRouteService.delete(camelRoute);
					}
				}

				Map<String,Route> routeMap = Maps.newHashMap();
				memoryList.stream().forEach(item->{
					routeMap.put(item.getId(),item);
				});

				memoryIdList.forEach(item->{
					if (!dbIdList.contains(item)){
						//db 中不存在，memory 中存在，需要加入到 db 中

						Route route = routeMap.get(item);

						CamelRoute camelRoute = trans2CamelRoute(route);

						camelRouteService.save(camelRoute);
					}
				});
			}

		}

		dbList.clear();
		dbList = camelRouteService.findAll();

		dbList.stream().forEach(item->{
			if (ServiceStatus.Started.name().equals(item.getStatus())){
				routeUtil.start(item.getRouteId());
			}else if (ServiceStatus.Stopped.name().equals(item.getStatus())){
				routeUtil.stop(item.getRouteId());
			}
		});



	}





	private CamelRoute trans2CamelRoute(Route route){
		CamelRoute camelRoute = new CamelRoute();

		EventDrivenConsumerRoute routeTemp = ((EventDrivenConsumerRoute) route);

		Map<String, Object> map = route.getProperties();

		String description = MapUtil.getStr(map, "description");

		camelRoute.setRouteId(route.getId());
		camelRoute.setDescription(description);
		camelRoute.setStatus(routeTemp.getStatus().toString());
		camelRoute.setCreateTime(new Date());

		return camelRoute;
	}














}
