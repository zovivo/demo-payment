package vn.vnpay.process.service.impl;

import vn.vnpay.process.entity.BaseEntity;
import vn.vnpay.process.repository.BaseRepository;
import vn.vnpay.process.service.BaseService;

import java.io.Serializable;
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

}
