package com.vrp.demo.service;


import com.vrp.demo.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

public interface BaseService<E extends BaseEntity, ID extends Serializable> {

    public List<E> findAll();

    public E find(ID id);

    public E create(E entity);

    public E update(E entity);

    public int deleteById(ID id);

    public boolean isUnique(Class<E> entityClass, Long id, String fieldName, String value);

}
