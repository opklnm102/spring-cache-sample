package me.dong.springboot2rediscache.support.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;

import java.util.Map;

/**
 * Rest API 통신 결과를 caching
 * <p>
 * Created by ethan.kim on 2018. 5. 19..
 */
@Component
public class CacheRestClient {

    public static final String CACHE_NAME = "cache.network";

    public static final String CACHE_TTL = "${cache.network.timetolive:300}";

    @Cacheable(value = CACHE_NAME, key = "#url")
    public <R> R get(RestOperations operations, String url, Class<R> responseType) {
        return operations.getForObject(url, responseType);
    }

    public <R, T> R post(RestOperations operations, String url, Class<R> responseType, T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body);
        return operations.postForObject(url, requestEntity, responseType);
    }

    @CacheEvict(value = CACHE_NAME, key = "#url")
    public <T> void put(RestOperations operations, String url, T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body);
        operations.put(url, requestEntity);
    }

    @CacheEvict(value = CACHE_NAME, key = "#url")
    public void delete(RestOperations operations, String url) {
        operations.delete(url);
    }

    public <R, T> R post(RestOperations operations, T body, MultiValueMap<String, String> headers, String url, Class<R> responseType, Map<String, ?> uriParams) {
        return exchange(operations, body, headers, url, HttpMethod.POST, responseType, uriParams);
    }

    @Cacheable(value = CACHE_NAME, key = "T(String).concat(#url).concat(#headers).concat(#uriParams)")
    public <R> R get(RestOperations operations, MultiValueMap<String, String> headers, String url, Class<R> responseType, Map<String, ?> uriParams) {
        return exchange(operations, headers, url, HttpMethod.GET, responseType, uriParams);
    }

    public <R, T> R put(RestOperations operations, T body, MultiValueMap<String, String> headers, String url, Class<R> responseType, Map<String, ?> uriParams) {
        return exchange(operations, body, headers, url, HttpMethod.PUT, responseType, uriParams);
    }

    public <R> R delete(RestOperations operations, MultiValueMap<String, String> headers, String url, Class<R> responseType, Map<String, ?> uriParams) {
        return exchange(operations, headers, url, HttpMethod.DELETE, responseType, uriParams);
    }

    private <R> R exchange(RestOperations operations, MultiValueMap<String, String> headers, String url, HttpMethod method, Class<R> responseType, Map<String, ?> uriParams) {
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<R> response = operations.exchange(url, method, request, responseType, uriParams);
        HttpStatus httpStatus = response.getStatusCode();

        Assert.state(httpStatus.is2xxSuccessful(), "failed to http 200 not ok");

        return response.getBody();
    }

    private <R, T> R exchange(RestOperations operations, T body, MultiValueMap<String, String> headers, String url, HttpMethod method, Class<R> responseType, Map<String, ?> uriParams) {
        HttpEntity<T> request = new HttpEntity<>(body, headers);
        ResponseEntity<R> response = operations.exchange(url, method, request, responseType);
        HttpStatus httpStatus = response.getStatusCode();

        Assert.state(httpStatus.is2xxSuccessful(), "failed to http 200 not ok");

        return response.getBody();
    }
}