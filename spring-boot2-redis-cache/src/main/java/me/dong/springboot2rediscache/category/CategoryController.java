package me.dong.springboot2rediscache.category;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by ethan.kim on 2018. 5. 19..
 */
@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(value = "/{categoryId}")
    public Category readCategory(@PathVariable long categoryId) {
        return categoryService.readCategory(categoryId);
    }

    @GetMapping
    public List<Category> readCategories() {
        return categoryService.readCategories();
    }

    @PostMapping
    public void addCategory() {
        categoryService.addCategory();
    }
}
