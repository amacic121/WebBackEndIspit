package amacic.myResources;

import amacic.data.Post;
import amacic.services.PostService.PostService;
import amacic.utils.StringUtil;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/posts")
public class PostResource {

    @Inject
    private PostService postService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/addPost")
    public Post addPost(Post post) {
        return this.postService.addPost(post);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/listAllPosts")
    public List<Post> listAllPosts(@DefaultValue("0") @QueryParam("offset") int offset, @DefaultValue("5") @QueryParam("limit") int limit, @QueryParam("text") String text, @QueryParam("tag") String tag) {
        if (!StringUtil.isEmpty(text)) {
            return this.postService.listPostsByText(offset, limit, text);
        } else if (!StringUtil.isEmpty(tag)) {
            return this.postService.listPostsByTag(offset, limit, tag);
        } else {
            return this.postService.listAllPosts(offset, limit);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getPostById")
    public Post getPostById(@QueryParam("postId") long postId)
    {
        return this.postService.getPostById(postId);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/editPost")
    public Post editPost(Post post) {
        return this.postService.editPost(post);
    }

    @DELETE
    @Path("/deletePost")
    public void deletePost(@QueryParam("postId") long postId) {
        this.postService.deletePost(postId);
    }

}
