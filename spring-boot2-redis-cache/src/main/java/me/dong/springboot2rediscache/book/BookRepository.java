package me.dong.springboot2rediscache.book;

import java.util.List;

/**
 * Created by ethan.kim on 2018. 5. 16..
 */
public interface BookRepository {

    Book findByIsbn(String isbn);

    List<Book> findAll();

    void save(Book book);
}
