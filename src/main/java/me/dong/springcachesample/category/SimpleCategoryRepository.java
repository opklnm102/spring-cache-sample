package me.dong.springcachesample.category;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ethan.kim on 2018. 5. 17..
 */
@Component
@CacheConfig(cacheNames = "category")
@Slf4j
public class SimpleCategoryRepository implements CategoryRepository {

    private static final String CATEGORY_KEY_FORMAT = "category:%d";

    // dummy data
    private Map<Long, Category> categories;

    public SimpleCategoryRepository() {
        this.categories = Stream.of(
                new Category(1, "Some Category1"),
                new Category(2, "Some Category2"))
                .collect(Collectors.toMap(Category::getId, Function.identity()));
    }

    // custom key를 지정할 수 있다
    @Cacheable(value = "category", key = "T(me.dong.springcachesample.category.SimpleCategoryRepository).createKey(#categoryId)")
    @Override
    public Category findById(long categoryId) {
        long start = System.currentTimeMillis();
        simulateSlowService();
        long end = System.currentTimeMillis();

        log.info("수행시간 {}", end - start);

        return categories.getOrDefault(categoryId, Category.EMPTY);
    }

    @Cacheable  // class의 @CacheConfig 설정이 적용되어 cacheName을 지정할 필요가 없다
    @Override
    public List<Category> findAll() {
        return new ArrayList<>(categories.values());
    }

    @CacheEvict(value = "category")
    @Override
    public void save(Category category) {
        log.info("{}", category);
    }

    private void simulateSlowService() {
        try {
            long time = 3000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String createKey(Long categoryId) {
        return String.format(CATEGORY_KEY_FORMAT, categoryId);
    }
}
