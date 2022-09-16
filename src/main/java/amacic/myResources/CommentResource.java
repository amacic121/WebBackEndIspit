package amacic.myResources;

import amacic.data.Comment;
import amacic.services.CommentService.CommentService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/comments")
public class CommentResource {

    @Inject
    private CommentService commentService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/addComment")
    public Comment addComment(Comment comment) {
        return this.commentService.addComment(comment);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/editComment")
    public Comment editComment(Comment comment) {
        return this.commentService.editComment(comment);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/listCommentsByPostId")
    public List<Comment> listCommentsByPostId(long postId, @DefaultValue("0") @QueryParam("offset") int offset, @DefaultValue("5") @QueryParam("limit") int limit) {
        return this.commentService.listCommentsByPostId(postId, offset, limit);
    }

    @DELETE
    @Path("/deleteComment")
    public void deleteComment(long commentId) {
        this.commentService.deleteComment(commentId);
    }
}
