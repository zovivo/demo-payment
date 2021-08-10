package vn.vnpay.preprocess.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vn.vnpay.preprocess.model.Payment;
import vn.vnpay.preprocess.service.PaymentRedisService;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "paymentRedisService")
public class PaymentRedisServiceImpl implements PaymentRedisService {

    private static final Logger logger = LogManager.getLogger(PaymentRedisServiceImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;
    private HashOperations hashOperations;

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public Payment getByTokenKey(String tokenKey) {
        logger.info("get Payment By tokenKey from Redis");
        Payment payment = (Payment) redisTemplate.opsForValue().get(tokenKey);
        return payment;
    }


}
