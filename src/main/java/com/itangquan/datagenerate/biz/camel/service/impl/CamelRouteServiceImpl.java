package com.itangquan.datagenerate.biz.camel.service.impl;

import com.itangquan.datagenerate.base.BaseServiceImpl;
import com.itangquan.datagenerate.biz.camel.RouteUtil;
import com.itangquan.datagenerate.biz.camel.entity.CamelRoute;
import com.itangquan.datagenerate.biz.camel.repository.CamelRouteRepository;
import com.itangquan.datagenerate.biz.camel.service.CamelRouteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ServiceStatus;
import org.apache.camel.impl.EventDrivenConsumerRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * camel路由 服务实现类
 * </p>
 *
 * @author code-generate-tool
 * @since 2019-06-27
 */
@Service
@Slf4j
public class CamelRouteServiceImpl extends BaseServiceImpl<CamelRoute,CamelRouteRepository> implements CamelRouteService {

	@Autowired
	CamelContext camelContext;

	@Autowired
	RouteUtil routeUtil;

	/**
	 * 更新 camel 路由状态
	 *
	 * @param routeId
	 */
	@Override
	public void triggerRouteStatus(String routeId) {
		CamelRoute routeTemp = new CamelRoute();
		routeTemp.setRouteId(routeId);

		CamelRoute one = this.one(routeTemp);

		EventDrivenConsumerRoute route = ((EventDrivenConsumerRoute) camelContext.getRoute(routeId));

		if (ServiceStatus.Started==route.getStatus()){
			routeUtil.stop(routeId);
			one.setStatus(ServiceStatus.Stopped.name());
		}else if (ServiceStatus.Stopped==route.getStatus()){
			routeUtil.start(routeId);
			one.setStatus(ServiceStatus.Started.name());
		}

		//更新表中的状态
		this.save(one);

	}


























}
