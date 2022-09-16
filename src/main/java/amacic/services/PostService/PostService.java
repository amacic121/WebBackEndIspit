package amacic.services.PostService;

import amacic.data.Post;

import java.util.List;

public interface PostService {

    public Post addPost(Post post);

    public List<Post> listAllPosts(int offset, int limit);

    public List<Post> listPostsByText(int offset, int limit, String text);

    public List<Post> listPostsByTag(int offset, int limit, String tag);

    public Post getPostById (long postId);

    public Post editPost(Post post);

    public void deletePost(long postId);
}
