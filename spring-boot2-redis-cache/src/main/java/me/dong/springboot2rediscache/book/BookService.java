package me.dong.springboot2rediscache.book;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

import static me.dong.springboot2rediscache.book.BookService.CACHE_NAME;

/**
 * Created by ethan.kim on 2018. 5. 17..
 */
@Service
@CacheConfig(cacheNames = CACHE_NAME)
public class BookService {

    public static final String CACHE_NAME = "cache.book";

    public static final Class CACHE_TYPE = Book.class;

    public static final String CACHE_TTL = "${cache.book.timetolive:60}";

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Cacheable(value = CACHE_NAME, key = "#isbn")
    public Book readBook(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Cacheable(value = CACHE_NAME)
    public List<Book> readBooks() {
        return bookRepository.findAll();
    }

    // data가 update되므로 cache 제거
    @Caching(evict = {
            @CacheEvict(value = CACHE_NAME, key = "#book.isbn"),  // cache.book::{isbn} 만 제거
            @CacheEvict(value = CACHE_NAME, allEntries = true)  // cache.book의 cache를 모두 제거
    })
    public void modifyBook(Book book) {
        bookRepository.save(book);
    }
}
