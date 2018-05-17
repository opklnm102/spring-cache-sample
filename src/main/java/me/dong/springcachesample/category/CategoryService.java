package me.dong.springcachesample.category;

/**
 * Created by ethan.kim on 2018. 5. 17..
 */
public class CategoryService {

    public static final String CACHE_NAME = "cache.category";

    public static final Class CACHE_TYPE = Category.class;

    public static final String CACHE_TTL = "${cache.category.timetolive:60}";

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category readCategory(long id) {
        return categoryRepository.findById(id);
    }


}
