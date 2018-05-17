package me.dong.springcachesample.book;

/**
 * Created by ethan.kim on 2018. 5. 16..
 */
public interface BookRepository {

    Book findByIsbn(String isbn);

    void save(Book book);
}
