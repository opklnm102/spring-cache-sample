package me.dong.springboot2rediscache.book;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by ethan.kim on 2018. 5. 17..
 */
@RestController
@RequestMapping(value = "/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> readBook() {
        return bookService.readBooks();
    }

    @GetMapping(value = "/{isbn}")
    public Book readBook(@PathVariable String isbn) {
        return bookService.readBook(isbn);
    }

    @PutMapping(value = "/{isbn}")
    public void modifyBook(@PathVariable String isbn) {
        Book book = Book.builder()
                .isbn(isbn)
                .title("Some Book")
                .build();

        bookService.modifyBook(book);
    }
}
