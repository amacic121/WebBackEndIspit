package amacic.services.CategoryService;

import amacic.data.Category;
import amacic.repo.CategoryRepo.CategoryRepository;

import javax.inject.Inject;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    @Inject
    CategoryRepository categoryRepository;

    public Category addCategory(Category category) {
        category.validate();
        return this.categoryRepository.addCategory(category);
    }

    public Category updateCategory(Category category) {
        return this.categoryRepository.updateCategory(category);
    }

    public List<Category> listAllCategories(int offset, int limit) {
        return this.categoryRepository.listAllCategories(offset, limit);
    }

    public void deleteCategory(long categoryId) {
        this.categoryRepository.deleteCategory(categoryId);
    }

    public Category findCategoryByPostId(long postId){
        return this.categoryRepository.findCategoryByPostId(postId);
    }

}
