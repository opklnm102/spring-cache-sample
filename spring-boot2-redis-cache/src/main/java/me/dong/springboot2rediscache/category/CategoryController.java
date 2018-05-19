package me.dong.springboot2rediscache.category;

import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ethan.kim on 2018. 5. 19..
 */
@RestController
@RequestMapping(value = "/categories")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    private final CacheManager cacheManager;

    public CategoryController(CategoryService categoryService, CacheManager cacheManager) {
        this.categoryService = categoryService;
        this.cacheManager = cacheManager;
    }

    @GetMapping(value = "/{categoryId}")
    public Category readCategory(@PathVariable long categoryId) {
        Category category = categoryService.readCategory(categoryId);

        cacheManager.getCacheNames().stream()
                .map(cacheManager::getCache)
                .map(cache -> cache.getNativeCache())
                .forEach(nativeCache -> log.info("{}", nativeCache));

        return category;
    }

    @GetMapping
    public List<Category> readCategories() {
        List<Category> categories=  categoryService.readCategories();

        cacheManager.getCacheNames().stream()
                .map(cacheManager::getCache)
                .map(cache -> cache.getNativeCache())
                .forEach(nativeCache -> log.info("{}", nativeCache));

        return categories;
    }

    @PostMapping
    public void addCategory() {
        categoryService.addCategory();

        cacheManager.getCacheNames().stream()
                .map(cacheManager::getCache)
                .map(cache -> cache.getNativeCache())
                .forEach(nativeCache -> log.info("{}", nativeCache));
    }
}
