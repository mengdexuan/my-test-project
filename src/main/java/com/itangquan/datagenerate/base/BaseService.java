package com.itangquan.datagenerate.base;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 基础服务接口类，将DAO层的方法进行提升
 * @param <T>
 */
public interface BaseService<T>{

	T get(Long id);

	@Transactional(rollbackFor = Exception.class)
	void delete(Long id);

	@Transactional(rollbackFor = Exception.class)
	void delete(T t);

	@Transactional(rollbackFor = Exception.class)
	void delete(Iterable<T> entities);

	@Transactional(rollbackFor = Exception.class)
	void update(T t);

	@Transactional(rollbackFor = Exception.class)
	void save(T t);

	@Transactional(rollbackFor = Exception.class)
	void save(Iterable<T> entities);

	T getOne(Specification<T> specification);

	T getOne(String property, Object value);

	T one(T t);

	/**
	 * 根据Map获取实体
	 * Map:
	 * 		key			val
	 * 		属性名称		属性值
	 * @param map
	 * @return
	 */
	T one(Map<String,Object> map,Class<T> bean);

	List<T> list(T t);

	/**
	 * 根据Map获取实体
	 * Map:
	 * 		key			val
	 * 		属性名称		属性值
	 * @param map
	 * @return
	 */

	List<T> list(Map<String, Object> map, Class<T> bean);

	void flush();

	List<T> findAll();

	List<T> findAll(String property, Object value);

	List<T> findAll(Specification<T> specification);

	boolean exists(Specification<T> specification);

	long count(T t);

	boolean exists(T t);

	boolean existsById(Long id);

}
