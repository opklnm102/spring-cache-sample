package me.dong.springcachesample.category;

import java.util.List;

/**
 * Created by ethan.kim on 2018. 5. 17..
 */
public interface CategoryRepository {

    Category findById(long id);

    List<Category> findAll();

    void save(Category category);
}