package amacic.repo.CommentRepo;

import amacic.data.Comment;

import java.util.List;

public interface CommentRepository {
    public Comment addComment(Comment comment);

    public Comment editComment(Comment comment);

    public List<Comment> listCommentsByPostId(long postId, int offset, int limit);

    public void deleteComment(long commentId);
}
