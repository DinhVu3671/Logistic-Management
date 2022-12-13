package com.vrp.demo.service.imp;

import com.vrp.demo.entity.BaseEntity;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.handler.CustomRestExceptionHandler;
import com.vrp.demo.models.BaseModel;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.search.BaseSearch;
import com.vrp.demo.repository.BaseRepository;
import com.vrp.demo.service.BaseService;
import com.vrp.demo.utils.CommonUtils;
import com.vrp.demo.utils.QueryTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import javax.persistence.NoResultException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

public abstract class BaseServiceImp<R extends BaseRepository<E, ID>, E extends BaseEntity, ID extends Serializable> implements BaseService<E, ID> {

    public abstract R getRepository();

    protected QueryTemplate getBaseQuery(BaseSearch search) {
        QueryTemplate queryTemplate = new QueryTemplate();
        Class entityClass = getRepository().getEntityClass();
        String query = " from " + entityClass.getSimpleName() + " e where 1=1 ";
        HashMap<String, Object> params = queryTemplate.getParameterMap();
        if (search.getId() != null && search.getId() > 0) {
            query += " and e.id = :id ";
            params.put("id", search.getId());
        }
        if (search.getFromCreatedAt() != null && search.getFromCreatedAt() > 0) {
            query += " and e.createdAt >= :fromCreatedAt ";
            params.put("fromCreatedAt", search.getFromCreatedAt());
        }
        if (search.getToCreatedAt() != null && search.getToCreatedAt() > 0) {
            query += " and e.createdAt < :toCreatedAt ";
            params.put("toCreatedAt", search.getToCreatedAt());
        }
        if (search.getFromUpdatedAt() != null && search.getFromUpdatedAt() > 0) {
            query += " and e.updatedAt >= :fromUpdatedAt ";
            params.put("fromUpdatedAt", search.getFromUpdatedAt());
        }
        if (search.getToUpdatedAt() != null && search.getToUpdatedAt() > 0) {
            query += " and e.updatedAt < :toUpdatedAt ";
            params.put("toUpdatedAt", search.getToUpdatedAt());
        }
        if (search.getIds() != null && search.getIds().size() > 0) {
            query += " and e.id in :ids";
            params.put("ids", search.getIds());
        }
        queryTemplate.setPageable(search.getPageable());
        queryTemplate.setQuery(query);
        queryTemplate.setParameterMap(params);
        return queryTemplate;
    }

    public void checkExistedId(BaseModel model, Code errorCode) throws CustomException {
        if (model.getId() == null)
            return;
        if (idIsExisted(getRepository().getEntityClass(), model.getId()))
            throw CommonUtils.createException(errorCode);
    }

    public boolean idIsExisted(Class<E> entityClass, Long id) {
        QueryTemplate queryTemplate = new QueryTemplate();
        String query = "select count(*) from " + entityClass.getSimpleName() + " e where e.id = " + id;
        queryTemplate.setQuery(query);
        return getRepository().count(queryTemplate) > 0;
    }

    public boolean isUnique(Class<E> entityClass, Long id, String fieldName, String value) {
        QueryTemplate queryTemplate = new QueryTemplate();
        String query = "select count(*) from " + entityClass.getSimpleName() + " e where e." + fieldName + " = :" + fieldName;
        HashMap<String, Object> params = queryTemplate.getParameterMap();
        params.put(fieldName, value);
        if (id != null) {
            query += " and e.id != :id";
            params.put("id", id);
        }
        queryTemplate.setQuery(query);
        queryTemplate.setParameterMap(params);
        return !(getRepository().count(queryTemplate) > 0);
    }

    @Override
    public List<E> findAll() {
        return getRepository().findAll();
    }

    @Override
    public E find(ID id) {
        return getRepository().find(id);
    }

    @Override
    public E create(E entity) {
        return getRepository().insert(entity);
    }

    @Override
    public E update(E entity) {
        return getRepository().update(entity);
    }

    @Override
    public int deleteById(ID id) {
        return getRepository().delete(id);
    }

    public List<E> find(QueryTemplate queryTemplate) {
        return getRepository().find(queryTemplate);
    }

    public E findOne(QueryTemplate queryTemplate) {
        return getRepository().findOne(queryTemplate);
    }

    public Page<E> search(QueryTemplate queryTemplate) {
        return getRepository().search(queryTemplate);
    }

}
