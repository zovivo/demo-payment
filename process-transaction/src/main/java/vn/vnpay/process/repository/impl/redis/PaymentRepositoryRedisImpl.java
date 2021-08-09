package vn.vnpay.process.repository.impl.redis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import vn.vnpay.process.entity.Payment;
import vn.vnpay.process.repository.PaymentRepository;
import vn.vnpay.process.util.CommonUtils;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository(value = "paymentRepositoryRedis")
public class PaymentRepositoryRedisImpl implements PaymentRepository {

    private static final Logger logger = LogManager.getLogger(PaymentRepositoryRedisImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;
    private HashOperations hashOperations;

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public Class getEntityClass() {
        return Payment.class;
    }

    @Override
    public Payment insert(Payment payment) {
        hashOperations.put(getEntityClass().getSimpleName(), payment.getId(), payment);
        logger.info("save payment to redis: {}", CommonUtils.parseObjectToString(payment));
        return payment;
    }

    @Override
    public Payment find(Long id) {
        return (Payment) hashOperations.get(getEntityClass().getSimpleName(), id);
    }

    @Override
    public List<Payment> findAll() {
        return hashOperations.values(getEntityClass().getSimpleName());
    }

    @Override
    public long countAll() {
        return hashOperations.values(getEntityClass().getSimpleName()).size();
    }

    @Override
    public Payment update(Payment payment) {
        hashOperations.put(getEntityClass().getSimpleName(), payment.getId(), payment);
        return payment;
    }

    @Override
    public int delete(Long id) {
        hashOperations.delete(getEntityClass().getSimpleName(), id);
        return 0;
    }

    @Override
    public Payment getByTokenKey(String tokenKey) {
        List<Payment> payments = hashOperations.values(getEntityClass().getSimpleName());
        if (payments.size() > 0)
            return payments.stream().filter(payment -> payment.getTokenKey().equals(tokenKey))
                    .sorted((Comparator.comparingLong(Payment::getId)).reversed())
                    .collect(Collectors.toList()).get(0);
        else
            return null;
    }


    @Override
    public List<Payment> update(List<Payment> entity) {
        return null;
    }

    @Override
    public int delete(Payment entity) {
        return 0;
    }

}
