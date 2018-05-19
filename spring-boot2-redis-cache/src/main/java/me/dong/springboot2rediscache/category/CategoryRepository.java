package me.dong.springboot2rediscache.category;

import java.util.List;

/**
 * Repository interface -> 추후 JpaRepository가 될 수 있다
 * <p>
 * Created by ethan.kim on 2018. 5. 17..
 */
public interface CategoryRepository {

    Category findById(long id);

    List<Category> findAll();

    void save(Category category);
}