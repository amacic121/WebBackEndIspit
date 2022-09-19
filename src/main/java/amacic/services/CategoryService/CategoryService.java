package amacic.services.CategoryService;

import amacic.data.Category;

import java.util.List;

public interface CategoryService {
    public Category addCategory(Category category);

    public Category updateCategory(Category category);

    public List<Category> listAllCategories(int offset, int limit);

    public void deleteCategory(long categoryId);

    public Category findCategoryByPostId(long postId);

    public int findCategoryNumber();
}
