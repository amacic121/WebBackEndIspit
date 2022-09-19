package amacic.myResources;

import amacic.data.Category;
import amacic.services.CategoryService.CategoryService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/categories")
public class CategoryResource {

    @Inject
    private CategoryService categoryService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/addCategory")
    public Category addCategory(Category category) {
        return this.categoryService.addCategory(category);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updateCategory")
    public Category updateCategory(Category category) {
        return this.categoryService.updateCategory(category);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/listAllCategories")
    public List<Category> listAllCategories(@DefaultValue("0") @QueryParam("offset") int offset, @DefaultValue("5") @QueryParam("limit") int limit) {
        return this.categoryService.listAllCategories(offset, limit);
    }

    @DELETE
    @Path("/deleteCategory")
    public void delete(@QueryParam("categoryId") long categoryId) {
        this.categoryService.deleteCategory(categoryId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/findCategoryByPost")
    public Category findCategoryByPost(long postId){
        return this.categoryService.findCategoryByPostId(postId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/findCategoryNumber")
    public int findCategoryNumber() {return this.categoryService.findCategoryNumber();
    }
}







