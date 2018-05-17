package me.dong.springcachesample.book;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by ethan.kim on 2018. 5. 16..
 */
@Builder
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "isbn")
public class Book {

    private String isbn;

    private String title;
}
