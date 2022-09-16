package amacic.services.PostService;

import amacic.data.Category;
import amacic.data.Comment;
import amacic.data.Post;
import amacic.data.Tag;
import amacic.repo.CategoryRepo.CategoryRepository;
import amacic.repo.CommentRepo.CommentRepository;
import amacic.repo.PostRepo.PostRepository;
import amacic.repo.PostTagRepo.PostTagRepository;
import amacic.repo.TagRepo.TagRepository;
import amacic.utils.StringUtil;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostServiceImpl implements PostService {

    @Inject
    private PostRepository postRepository;
    @Inject
    private TagRepository tagRepository;
    @Inject
    private PostTagRepository postTagRepository;
    @Inject
    private CategoryRepository categoryRepository;
    @Inject
    private CommentRepository commentRepository;

    public Post addPost(Post post) {
        post.validate();
        Post finalPost = this.postRepository.addPost(post);
        List<String> tags = new ArrayList<>();
        Arrays.stream(post.getTagsString().split(",")).forEach((String tagString) -> {
            if (!StringUtil.isEmpty(tagString)) {
                Tag tag = this.tagRepository.createTagIfNameNotExist(new Tag(0, tagString.trim()));
                tags.add(tag.getValue());
                this.postTagRepository.addPostTag(finalPost.getId(), tag.getId());
            }
        });

        finalPost.setTagsString(String.join(", ", tags));
        return finalPost;
    }

    public List<Post> listAllPosts(int offset, int limit) {
        return this.postRepository.listAllPosts(offset, limit);
    }

    public List<Post> listPostsByText(int offset, int limit, String text) {
        return this.postRepository.listPostsByText(offset, limit, text);
    }

    public List<Post> listPostsByTag(int offset, int limit, String tag) {
        return this.postRepository.listPostsByTag(offset, limit, tag);
    }

    public Post getPostById(long postId) {
        Category category = this.categoryRepository.findCategoryByPostId(postId);
        List<Tag> tags = this.tagRepository.findTagsByPostId(postId);
        List<Comment> comments = this.commentRepository.listCommentsByPostId(postId, 0, 10000);
        Post post = this.postRepository.getPostById(postId);
        post.setCategory(category);
        post.setComments(comments);

        StringBuilder sb = new StringBuilder();
        for (Tag tag : tags) {
            sb.append(tag.getValue());
        }
        String tagsString = sb.toString();
        post.setTagsString(tagsString);

        return post;
    }


    public Post editPost(Post post) {
        post.validate();
        return this.postRepository.updatePost(post);
    }

    public void deletePost(long postId) {
        this.postRepository.deletePost(postId);
        this.postTagRepository.removePostFromTag(postId);
    }

}
