package vn.vnpay.payment.service.impl;

import org.springframework.data.domain.Page;
import vn.vnpay.payment.entity.BaseEntity;
import vn.vnpay.payment.exception.CustomException;
import vn.vnpay.payment.repository.BaseRepository;
import vn.vnpay.payment.service.BaseService;
import vn.vnpay.payment.util.QueryTemplate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public abstract class BaseServiceImpl<R extends BaseRepository<E, ID>, E extends BaseEntity, ID extends Serializable> implements BaseService<E, ID> {

    public abstract R getRepository();


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

    public List<E> find(QueryTemplate queryTemplate) {
        return getRepository().find(queryTemplate);
    }

    public Page<E> search(QueryTemplate queryTemplate) {
        return getRepository().search(queryTemplate);
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

    public boolean idIsExisted(Class<E> entityClass, Long id) {
        QueryTemplate queryTemplate = new QueryTemplate();
        String query = "select count(*) from " + entityClass.getSimpleName() + " e where e.id = " + id;
        queryTemplate.setQuery(query);
        return getRepository().count(queryTemplate) > 0;
    }

}
