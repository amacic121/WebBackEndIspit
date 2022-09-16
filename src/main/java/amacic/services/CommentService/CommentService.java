package amacic.services.CommentService;

import amacic.data.Comment;

import java.util.List;

public interface CommentService {
    public Comment addComment(Comment comment);

    public void deleteComment(long commentId);

    public Comment editComment(Comment comment);

    public List<Comment> listCommentsByPostId(long postId, int offset, int limit);
}
