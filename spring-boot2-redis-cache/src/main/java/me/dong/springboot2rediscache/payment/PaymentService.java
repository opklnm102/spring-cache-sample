package me.dong.springboot2rediscache.payment;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import lombok.extern.slf4j.Slf4j;
import me.dong.springboot2rediscache.support.cache.CacheRestClient;

/**
 * 외부 Application과 통신하여 결제에 관한 처리를 담당
 * <p>
 * Created by ethan.kim on 2018. 5. 19..
 */
@Service
@Slf4j
public class PaymentService {

    private final CacheRestClient restClient;

    private final RestOperations restOperations;

    private static final String PAYMENT_URL = "localhost:8080/payments";

    public PaymentService(CacheRestClient restClient, RestOperations restOperations) {
        this.restClient = restClient;
        this.restOperations = restOperations;
    }

    public void readPaymentHistory() {
        Payment payment = restClient.get(restOperations, PAYMENT_URL, Payment.class);

        log.info("{}", payment);
    }
}
