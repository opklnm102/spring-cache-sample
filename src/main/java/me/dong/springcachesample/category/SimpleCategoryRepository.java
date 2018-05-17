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

    private Map<Long, Category> categories;

    public SimpleCategoryRepository() {
        this.categories = Stream.of(
                new Category(1, "Some Category1"),
                new Category(2, "Some Category2"))
                .collect(Collectors.toMap(Category::getId, Function.identity()));
    }

    @Cacheable(value = "category", key = "#id")
    @Override
    public Category findById(long id) {
        long start = System.currentTimeMillis();
        simulateSlowService();
        long end = System.currentTimeMillis();

        log.info("수행시간 {}", end - start);

        return categories.getOrDefault(id, Category.EMPTY);
    }

    @CacheEvict(value = "category")
    @Override
    public List<Category> findAll() {
        return new ArrayList<>(categories.values());
    }

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
}
