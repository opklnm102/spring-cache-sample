package me.dong.springcachesample.category;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static me.dong.springcachesample.category.CategoryService.CACHE_NAME;

/**
 * Created by ethan.kim on 2018. 5. 17..
 */
@CacheConfig(cacheNames = CACHE_NAME)
@Service
public class CategoryService {

    public static final String CACHE_NAME = "cache.category";

    public static final Class CACHE_TYPE = Category.class;

    public static final String CACHE_TTL = "${cache.category.timetolive:60}";

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // custom key를 지정할 수 있다
    @Cacheable(value = CACHE_NAME, key = "T(String).format('category:%d', #categoryId)")
    public Category readCategory(long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Cacheable  // class의 @CacheConfig 설정이 적용되어 cacheName을 지정할 필요가 없다
    public List<Category> readCategories() {
        return categoryRepository.findAll();
    }

    @CacheEvict(value = CACHE_NAME)
    public void addCategory() {
        Category category = new Category(3, "Some Category");
        categoryRepository.save(category);
    }
}
