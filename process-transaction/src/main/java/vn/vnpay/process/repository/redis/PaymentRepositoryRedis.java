package vn.vnpay.process.repository.redis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import vn.vnpay.process.entity.Payment;
import vn.vnpay.process.util.CommonUtils;

import javax.annotation.PostConstruct;

@Repository(value = "paymentRepositoryRedis")
public class PaymentRepositoryRedis {

    private static final Logger logger = LogManager.getLogger(PaymentRepositoryRedis.class);

    @Autowired
    private RedisTemplate redisTemplate;
    private HashOperations hashOperations;

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }


    private String getHashName() {
        return Payment.class.getSimpleName();
    }

    public Payment insert(Payment payment) {
        hashOperations.put(getHashName(), payment.getTokenKey(), CommonUtils.parseObjectToString(payment));
        redisTemplate.expireAt(getHashName(), CommonUtils.getEndDateTime());
        logger.info("save payment of hash {} to redis : {} expireTime: {}", getHashName(), CommonUtils.parseObjectToString(payment), CommonUtils.getEndDateTime());
        return payment;
    }

}
