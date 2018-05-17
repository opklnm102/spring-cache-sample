package me.dong.springcachesample.book;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ethan.kim on 2018. 5. 17..
 */
@Component
@CacheConfig(cacheNames = "books")
@Slf4j
public class SimpleBookRepository implements BookRepository {

    @Cacheable(value = "books", key = "#isbn")
    @Override
    public Book findByIsbn(String isbn) {
        long start = System.currentTimeMillis();
        simulateSlowService();
        long end = System.currentTimeMillis();

        log.info("수행시간 {}", end - start);

        return new Book(isbn, "Some Book");
    }

    @CacheEvict(value = "books", key = "#book.isbn")
    @Override
    public void save(Book book) {
        log.info("{}", book);
    }

    private void simulateSlowService() {
        try {
            long time = 3000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
