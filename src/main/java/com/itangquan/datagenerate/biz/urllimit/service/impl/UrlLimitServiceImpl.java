package com.itangquan.datagenerate.biz.urllimit.service.impl;

import com.itangquan.datagenerate.base.BaseServiceImpl;
import com.itangquan.datagenerate.biz.urllimit.entity.UrlLimit;
import com.itangquan.datagenerate.biz.urllimit.repository.UrlLimitRepository;
import com.itangquan.datagenerate.biz.urllimit.service.UrlLimitService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 接口限流 服务实现类
 * </p>
 *
 * @author adi
 * @since 2019-06-21
 */
@Service
public class UrlLimitServiceImpl extends BaseServiceImpl<UrlLimit,UrlLimitRepository> implements UrlLimitService {

}
