package com.itangquan.datagenerate.base;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.itangquan.datagenerate.base.exception.GlobalServiceException;
import com.itangquan.datagenerate.base.util.HelpMe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基础服务实现类
 *
 * @param <T>
 * @param <R>
 * @author LIQIU
 */
@SuppressWarnings("rawtypes")
public class BaseServiceImpl<T, R extends BaseRepository<T>> implements BaseService<T> {

	@Autowired
	protected R repository;

	public long count(T t){
		Example<T> example = Example.of(t);
		long count = repository.count(example);
		return count;
	}

	public boolean exists(T t){
		Example<T> example = Example.of(t);
		boolean exists = repository.exists(example);
		return exists;
	}

	public boolean existsById(Long id){
		return repository.existsById(id);
	}

	@Override
	public T get(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(T t) {
		repository.delete(t);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Iterable<T> entities) {
		repository.deleteAll(entities);
	}

	/**
	 * 更新对象
	 * 		1.先通过传入的对象的ID（修改时主键ID必传）查询出数据库中的对象
	 * 		2.再用传入的对象的属性值更新数据库中对象的值
	 * 		3.使用repository.save(persistentObj)将新值更新到数据库
	 * @param t
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(T t) {
		T target = null;

		try {
			target = ((Class<T>)t.getClass()).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		BeanUtil.copyProperties(t,target);

		Field[] fields = ReflectUtil.getFields(t.getClass());

		for (Field field:fields){
			Id id = field.getAnnotation(Id.class);
			//非主键字段的值全部设置为null
			if (id==null){
				ReflectUtil.setFieldValue(target,field,null);
			}else {
				Object val = ReflectUtil.getFieldValue(t, field);
				if (val==null){
					throw new GlobalServiceException("修改数据，id 必填！");
				}
			}
		}

		//只通过主键查询数据库中的对象
		T persistentObj = this.one(target);

		for (Field field:fields){
			Object val = ReflectUtil.getFieldValue(t, field);
			//如果传入的字段不为空，设置到查询出的数据库对象中
			if (val!=null){
				ReflectUtil.setFieldValue(persistentObj,field,val);
			}
		}

		repository.save(persistentObj);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(T t) {
		repository.save(t);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(Iterable<T> entities) {
		repository.saveAll(entities);
	}


	@Override
	public T getOne(Specification<T> specification) {
		return this.repository.findOne(specification).orElse(null);
	}

	@Override
	public T getOne(String property, Object value) {
		Specification<T> specification = (root, query, builder) -> builder.equal(root.get(property), value);
		return this.getOne(specification);
	}

	@Override
	public T one(T t) {
		List<T> list = this.list(t);
		if (HelpMe.isNotNull(list)){
			return list.get(list.size()-1);
		}
		return null;
	}

	@Override
	public List<T> list(T t) {
		Example<T> example = Example.of(t);
		List<T> list = this.repository.findAll(example);
		return list;
	}

	/**
	 * 根据Map获取实体
	 * Map:
	 * key			val
	 * 属性名称		属性值
	 *
	 * @param map
	 * @return
	 */
	@Override
	public T one(Map<String, Object> map,Class<T> bean) {
		List<T> list = this.list(map, bean);
		if (HelpMe.isNotNull(list)){
			return list.get(list.size()-1);
		}
		return null;
	}

	/**
	 * 根据Map获取实体
	 * Map:
	 * key			val
	 * 属性名称		属性值
	 *
	 * @param map
	 * @return
	 */
	@Override
	public List<T> list(Map<String, Object> map, Class<T> bean) {
		T instance = null;
		try {
			instance = bean.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		Method[] methods = ReflectUtil.getMethodsDirectly(bean,false);

		List<Method> methodList = Arrays.stream(methods).filter(method -> {
			return method.getName().startsWith("set");
		}).collect(Collectors.toList());


		T finalInstance = instance;
		methodList.stream().forEach(method -> {
			ReflectUtil.invoke(finalInstance,method,new Object[]{null});
		});

		BeanUtil.fillBeanWithMap(map,instance,true);

		return this.list(instance);
	}

	@Override
	public void flush() {
		this.repository.flush();
	}

	@Override
	public List<T> findAll() {
		return this.repository.findAll();
	}

	@Override
	public List<T> findAll(String property, Object value) {
		Specification<T> specification = (root, query, builder) -> builder.equal(root.get(property), value);
		return this.repository.findAll(specification);
	}

	@Override
	public List<T> findAll(Specification<T> specification) {
		return this.repository.findAll(specification);
	}

	@Override
	public boolean exists(Specification<T> specification) {
		return this.repository.count(specification) > 0;
	}






}
