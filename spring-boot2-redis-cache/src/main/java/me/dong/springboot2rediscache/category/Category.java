package me.dong.springboot2rediscache.category;

import java.io.Serializable;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by ethan.kim on 2018. 5. 17..
 */
@Builder
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class Category implements Serializable {

    public static final Category EMPTY = new Category();

    private long id;

    private String name;

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
