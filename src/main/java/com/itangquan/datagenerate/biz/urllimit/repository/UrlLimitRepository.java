package com.itangquan.datagenerate.biz.urllimit.repository;


import com.itangquan.datagenerate.base.BaseRepository;
import com.itangquan.datagenerate.biz.urllimit.entity.UrlLimit;

import java.util.List;

/**
 * <p>
 * 接口限流 Mapper 接口
 * </p>
 *
 * @author adi
 * @since 2019-06-21
 */
public interface UrlLimitRepository extends BaseRepository<UrlLimit> {

	List<UrlLimit> findByUrlLimitGreaterThan(Integer urlLimit);

}
