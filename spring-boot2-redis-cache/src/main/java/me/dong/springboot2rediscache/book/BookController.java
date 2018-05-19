package me.dong.springboot2rediscache.book;

import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ethan.kim on 2018. 5. 17..
 */
@RestController
@RequestMapping(value = "/books")
@Slf4j
public class BookController {

    private final BookService bookService;

    private final CacheManager cacheManager;

    public BookController(BookService bookService, CacheManager cacheManager) {
        this.bookService = bookService;
        this.cacheManager = cacheManager;
    }

    @GetMapping(value = "/{isbn}")
    public Book readBook(@PathVariable String isbn) {




        return bookService.readBook(isbn);
    }
}
