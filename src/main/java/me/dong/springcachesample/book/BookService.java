package me.dong.springcachesample.book;

import org.springframework.stereotype.Service;

/**
 * Created by ethan.kim on 2018. 5. 17..
 */
@Service
public class BookService {

    public static final String CACHE_NAME = "cache.book";

    public static final Class CACHE_TYPE = Book.class;

    public static final String CACHE_TTL = "${cache.book.timetolive:60}";

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book readBook(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
}
