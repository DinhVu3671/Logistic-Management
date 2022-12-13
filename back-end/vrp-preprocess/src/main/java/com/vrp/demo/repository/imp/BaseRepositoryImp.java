package com.vrp.demo.repository.imp;

import com.vrp.demo.entity.BaseEntity;
import com.vrp.demo.repository.BaseRepository;
import com.vrp.demo.service.imp.BaseServiceImp;
import com.vrp.demo.utils.QueryTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaUpdate;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BaseRepositoryImp<E extends BaseEntity, ID extends Serializable> implements BaseRepository<E, ID> {

    private Logger logger = LoggerFactory.getLogger(BaseServiceImp.class);

    @PersistenceContext
    private EntityManager entityManager;
    private final Class entityClass;

    public BaseRepositoryImp(Class entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public Class getEntityClass() {
        return entityClass;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected Page<E> wrapResult(List<E> results, Pageable page, long count) {
        if (results == null) {
            results = Collections.emptyList();
        }
        return new PageImpl<>(results, page, count);
    }

    //====== Build Query Coding ======

    public Query createQuery(QueryTemplate queryTemplate) {
        Query query;
        if (queryTemplate.isNative()) {
            if (queryTemplate.getResultType() != null) {
                query = entityManager.createNativeQuery(queryTemplate.getQuery(), queryTemplate.getResultType());
            } else {
                query = entityManager.createNativeQuery(queryTemplate.getQuery());
            }
        } else {
            if (queryTemplate.getResultType() != null) {
                query = entityManager.createQuery(queryTemplate.getQuery(), queryTemplate.getResultType());
            } else {
                query = entityManager.createQuery(queryTemplate.getQuery(), getEntityClass());
            }
        }
        HashMap<String, Object> params = queryTemplate.getParameterMap();
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        return query;
    }

    //====== End Build Query Coding ======

    //====== Create Coding ======
    public E insert(E entity) {
        entityManager.persist(entity);
        return entity;
    }

    public List<E> insert(List<E> listEntities, int batchSize) {
        if (listEntities != null && !listEntities.isEmpty()) {
            for (int i = 0; i < listEntities.size(); i++) {
                listEntities.set(i, insert(listEntities.get(i)));
                if (i % batchSize == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
            }
        }
        return listEntities;
    }

    public List<E> insert(List<E> listEntities) {
        return insert(listEntities, 50);
    }

    //====== End Create Coding ======

    //====== Research Coding ======

    @Override
    public E find(ID id) {
        return (E) entityManager.find(entityClass, id);
    }

    @Override
    public List<E> findAll() {
        Query createQuery = entityManager.createQuery("From " + this.entityClass.getSimpleName());
        return createQuery.getResultList();
    }

    @Override
    public long countAll() {
        String queryStr = "SELECT COUNT(e) from " + entityClass.getSimpleName() + " e";
        Query createQuery = entityManager.createQuery(queryStr);
        return (Long) createQuery.getSingleResult();
    }

    public long count(QueryTemplate queryTemplate) {
        queryTemplate.setResultType(Long.class);
        Query createQuery = createQuery(queryTemplate);
        return ((Number) createQuery.getSingleResult()).longValue();
    }

    @Override
    public List<E> find(QueryTemplate queryTemplate) {
        Query createQuery = createQuery(queryTemplate);
        return createQuery.getResultList();
    }

    @Override
    public Page<E> search(QueryTemplate queryTemplate) {
        Query createQuery = createQuery(queryTemplate);
        createQuery.setFirstResult((int) queryTemplate.getPageable().getOffset());
        createQuery.setMaxResults(queryTemplate.getPageable().getPageSize());
        List<E> results = createQuery.getResultList();
        String countQuery = "select count(*) " + queryTemplate.getQuery();
        queryTemplate.setQuery(countQuery);
        long count = count(queryTemplate);
        return wrapResult(results, queryTemplate.getPageable(), count);
    }

    public E findOne(QueryTemplate queryTemplate) {
        Query createQuery = createQuery(queryTemplate);
        E e;
        try {
            e = (E) createQuery.getSingleResult();
        } catch (NoResultException noResultException) {
            logger.info(noResultException.getMessage());
            return null;
        }
        return e;
    }

    //====== End Research Coding ======

    //====== Update Coding ======

    @Override
    public E update(E entity) {
        entityManager.merge(entity);
        return entity;
    }

    public List<E> update(List<E> listEntities, int batchSize) {
        if (listEntities != null && !listEntities.isEmpty()) {
            for (int i = 0; i < listEntities.size(); i++) {
                update(listEntities.get(i));
                if (i % batchSize == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
            }
        }
        return listEntities;
    }

    @Override
    public List<E> update(List<E> listEntities) {
        return update(listEntities, 50);
    }

    public int update(CriteriaUpdate criUpdate) {
        Query query = entityManager.createQuery(criUpdate);
        return query.executeUpdate();
    }

//    public int update(String queryStr, boolean isNative) {
//        Query query = buildQuery(queryStr, isNative);
//        return query.executeUpdate();
//    }

    //====== End Update Coding ======

    //====== Delete Coding ======

    @Override
    public int delete(E entity) {
        entityManager.remove(entity);
        return 1;
    }

    public int delete(CriteriaDelete criDelete) {
        Query query = entityManager.createQuery(criDelete);
        return query.executeUpdate();
    }

    public int deleteByQuery(String queryStr) {
        Query query = entityManager.createQuery(queryStr);
        return query.executeUpdate();
    }
//
//    public int deleteByQuery(String queryStr, boolean isNative, Map<String, Object> params) {
//        Query query = buildQueryHasParameters(queryStr, isNative, params);
//        return query.executeUpdate();
//    }

    @Override
    public int delete(ID id) {
        String queryStr = "Delete from " + entityClass.getSimpleName() + " e where e.id = " + id;
        Query query = entityManager.createQuery(queryStr);
        return query.executeUpdate();
    }

    //====== End Delete Coding ======

}
