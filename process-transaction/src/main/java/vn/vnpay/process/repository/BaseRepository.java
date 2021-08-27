package vn.vnpay.process.repository;

import vn.vnpay.process.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

public interface BaseRepository<E extends BaseEntity, ID extends Serializable> {

    public Class getEntityClass();

    public E insert(E e);

    public E find(ID id) ;

    public List<E> findAll();

    public long countAll();

    public E update(E entity);

    public List<E> update(List<E> entity);

    public int delete(E entity);

    public int delete(ID id);

}
