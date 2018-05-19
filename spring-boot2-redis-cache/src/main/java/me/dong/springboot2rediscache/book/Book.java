package me.dong.springboot2rediscache.book;

import java.io.Serializable;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by ethan.kim on 2018. 5. 16..
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "isbn")
public class Book implements Serializable {

    private String isbn;

    private String title;

    @Builder
    public Book(String isbn, String title) {
        this.isbn = isbn;
        this.title = title;
    }
}
