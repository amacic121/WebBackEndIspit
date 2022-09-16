package amacic.services.CommentService;

import amacic.data.Comment;
import amacic.repo.CommentRepo.CommentRepository;

import javax.inject.Inject;
import java.util.List;

public class CommentServiceImpl implements CommentService {

    @Inject
    CommentRepository commentRepository;

    @Override
    public Comment addComment(Comment comment) {
        comment.validate();
        return this.commentRepository.addComment(comment);
    }

    @Override
    public void deleteComment(long commentId) {
        this.commentRepository.deleteComment(commentId);
    }

    @Override
    public Comment editComment(Comment comment) {
        comment.validate();
        return this.commentRepository.editComment(comment);
    }

    @Override
    public List<Comment> listCommentsByPostId(long postId, int offset, int limit) {
        return this.commentRepository.listCommentsByPostId(postId, offset, limit);
    }
}
