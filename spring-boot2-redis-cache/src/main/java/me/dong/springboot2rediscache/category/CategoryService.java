package me.dong.springboot2rediscache.category;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import static me.dong.springboot2rediscache.category.CategoryService.CACHE_NAME;

/**
 * Created by ethan.kim on 2018. 5. 17..
 */
@CacheConfig(cacheNames = CACHE_NAME)
@Service
@Slf4j
public class CategoryService {

    public static final String CACHE_NAME = "cache.category";

    public static final String CACHE_TTL = "${cache.category.timetolive:60}";

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // custom key를 지정할 수 있다
    @Cacheable(value = CACHE_NAME, key = "T(String).format('category:%d', #categoryId)")
    public Category readCategory(long categoryId) {
        StopWatch stopWatch = new StopWatch(CACHE_NAME + ":" + categoryId);
        stopWatch.setKeepTaskList(true);

        stopWatch.start("Start read category");

        Category category = categoryRepository.findById(categoryId);

        stopWatch.stop();

        log.info(stopWatch.toString());
        log.info(stopWatch.prettyPrint());

        return category;
    }

    @Cacheable  // class의 @CacheConfig 설정이 적용되어 cacheName을 지정할 필요가 없다
    public List<Category> readCategories() {
        long start = System.currentTimeMillis();

        List<Category> categories = categoryRepository.findAll();

        log.info("수행시간 {}", System.currentTimeMillis() - start);

        return categories;
    }

    @CacheEvict(value = CACHE_NAME)
    public void addCategory() {
        Category category = new Category(3, "Some Category");
        categoryRepository.save(category);
    }
}
