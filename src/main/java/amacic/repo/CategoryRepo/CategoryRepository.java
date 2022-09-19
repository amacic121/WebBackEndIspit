package amacic.repo.CategoryRepo;

import amacic.data.Category;

import java.util.List;

public interface CategoryRepository {
    public Category addCategory(Category category);

    public Category updateCategory(Category category);

    public List<Category> listAllCategories(int offset, int limit);

    public void deleteCategory(long categoryId);

    public Category findCategoryByPostId(long postId);

    public int getPages();
}