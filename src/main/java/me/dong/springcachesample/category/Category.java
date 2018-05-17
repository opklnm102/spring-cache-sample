package me.dong.springcachesample.category;

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
public class Category {

    public static final Category EMPTY = new Category();

    private long id;

    private String name;
}
