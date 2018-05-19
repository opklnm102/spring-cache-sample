package me.dong.springboot2rediscache.book;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ethan.kim on 2018. 5. 17..
 */
@Component
@Slf4j
public class SimpleBookRepository implements BookRepository {

    @Override
    public Book findByIsbn(String isbn) {
        simulateSlowService();

        return Book.builder()
                .isbn(isbn)
                .title("Some Book")
                .build();
    }

    @Override
    public List<Book> findAll() {
        simulateSlowService();
        
        return Stream.iterate(0, n -> n + 1)
                .limit(10)
                .map(n -> Book.builder()
                        .isbn("isbn" + n)
                        .title("title" + n)
                        .build())
                .collect(Collectors.toList());
    }

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
