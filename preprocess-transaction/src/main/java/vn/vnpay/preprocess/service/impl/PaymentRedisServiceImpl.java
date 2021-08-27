package vn.vnpay.preprocess.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vn.vnpay.preprocess.model.Payment;
import vn.vnpay.preprocess.service.PaymentRedisService;
import vn.vnpay.preprocess.util.CommonUtils;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Service(value = "paymentRedisService")
public class PaymentRedisServiceImpl implements PaymentRedisService {

    private static final Logger logger = LogManager.getLogger(PaymentRedisServiceImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;
    private HashOperations hashOperations;

    @Override
    public Payment getByTokenKey(String tokenKey) {
        logger.info("get Payment By tokenKey from Redis");
        String paymentData = (String) hashOperations.get(getHashName(), tokenKey);
        Payment payment = CommonUtils.parseStringToObject(paymentData, Payment.class);
        logger.info("payment By tokenKey from Redis: {}", CommonUtils.parseObjectToString(payment));
        return payment;
    }

    @Override
    public boolean isExistedTokenKey(String tokenKey) {
        logger.info("begin check existed tokenKey in Redis");
        boolean isExisted = hashOperations.hasKey(getHashName(), tokenKey);
        if (true == isExisted) {
            logger.warn("tokenKey is existed");
        }
        logger.info("end check existed tokenKey in Redis");
        return isExisted;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    private String getHashName() {
        return Payment.class.getSimpleName();
    }


}
