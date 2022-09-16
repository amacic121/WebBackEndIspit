package amacic.data;

import amacic.exceptions.ValidationException;
import amacic.utils.PostUtil;
import amacic.utils.StringUtil;

public class Comment {

    private String author;
    private String text;
    private long createdAt;
    private Post post;
    private long id;

    public Comment() {
    }

    public Comment(long id, String author, String text, long createdAt, Post post) {
        this.id = id;
        this.author = author;
        this.text = text;
        this.createdAt = createdAt;
        this.post = post;
    }

    public void validate() {
        if (StringUtil.isEmpty(author)) {
            throw new ValidationException("Validation of comment unsuccessful, author is invalis");
        } else if (StringUtil.isEmpty(text)) {
            throw new ValidationException("Validation of comment unsuccessful, text is invalis");
        } else if (PostUtil.isEmpty(post)) {
            throw new ValidationException("Validation of comment unsuccessful, post is invalis");
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}