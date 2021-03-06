package me.dong.springboot2rediscache.category;

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
@Slf4j
public class SimpleCategoryRepository implements CategoryRepository {

    // dummy data
    private Map<Long, Category> categories;

    public SimpleCategoryRepository() {
        this.categories = Stream.of(
                new Category(1, "Some Category1"),
                new Category(2, "Some Category2"))
                .collect(Collectors.toMap(Category::getId, Function.identity()));
    }

    @Override
    public Category findById(long categoryId) {
        simulateSlowService();
        return categories.getOrDefault(categoryId, Category.EMPTY);
    }

    @Override
    public List<Category> findAll() {
        simulateSlowService();
        return new ArrayList<>(categories.values());
    }

    @Override
    public void save(Category category) {
        log.info("{}", category);
    }

    /**
     * slow query 상황을 재현하기 위한 메소드
     */
    private void simulateSlowService() {
        try {
            long time = 3000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
