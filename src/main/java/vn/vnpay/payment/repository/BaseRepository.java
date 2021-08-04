package vn.vnpay.payment.repository;

import org.springframework.data.domain.Page;
import vn.vnpay.payment.entity.BaseEntity;
import vn.vnpay.payment.util.QueryTemplate;

import java.io.Serializable;
import java.util.List;

public interface BaseRepository<E extends BaseEntity, ID extends Serializable> {

    public Class getEntityClass();

    public E insert(E e);

    public E find(ID id) ;

//    public E find(ID id, boolean lock);

    public List<E> findAll();

    public long countAll();

    public List<E> find(QueryTemplate queryTemplate);

    public Page<E> search(QueryTemplate queryTemplate);

    public E update(E entity);

    public List<E> update(List<E> entity);

    public int delete(E entity);

    public int delete(ID id);

    public long count(QueryTemplate queryTemplate);

}
