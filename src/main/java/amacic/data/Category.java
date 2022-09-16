package amacic.data;

import amacic.exceptions.ValidationException;
import amacic.utils.StringUtil;

import java.util.List;

public class Category {

    private String name;
    private String description;
    private long id;
    private List<Post> posts;

    public Category() {
    }

    public Category(long id, String name, String description, List<Post> posts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.posts = posts;
    }

    public void validate() {
        if (StringUtil.isEmpty(name)) {
            throw new ValidationException("Validation of category unsuccessful");
        } else if (StringUtil.isEmpty(description)) {
            throw new ValidationException("Validation of category unsuccessful");
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
