package com.vrp.demo.repository;

import com.vrp.demo.entity.BaseEntity;
import com.vrp.demo.utils.QueryTemplate;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

public interface BaseRepository<E extends BaseEntity, ID extends Serializable> {

    public Class getEntityClass();

    public E insert(E e);

    public List<E> insert(List<E> entities);

    public E find(ID id) ;

    public List<E> findAll();

    public long countAll();

    public List<E> find(QueryTemplate queryTemplate);

    public E findOne(QueryTemplate queryTemplate);

    public Page<E> search(QueryTemplate queryTemplate);

    public E update(E entity);

    public List<E> update(List<E> entity);

    public int delete(E entity);

    public int delete(ID id);

    public long count(QueryTemplate queryTemplate);

}
