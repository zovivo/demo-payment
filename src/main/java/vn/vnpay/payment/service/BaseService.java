package vn.vnpay.payment.service;


import vn.vnpay.payment.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

public interface BaseService<E extends BaseEntity, ID extends Serializable> {

    public List<E> findAll();

    public E find(ID id);

    public E create(E entity);

    public E update(E entity);

    public boolean isUnique(Class<E> entityClass, Long id, String fieldName, String value);

}
