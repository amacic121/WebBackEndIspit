package amacic.myResources;

import amacic.data.User;
import amacic.authentication.Login;
import amacic.services.UserService.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/users")
public class UserResource {

    @Inject
    private UserService userService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/addUser")
    public User addUser(User user) {
        return this.userService.addUser(user);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/editUser")
    public User editUser(User user) {
        return this.userService.editUser(user);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/findUserByEmail")
    public User findUserByEmail(@QueryParam("email") String email) {
        return this.userService.findUserByEmail(email);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/listAllUsers")
    public List<User> listAllUsers(@DefaultValue("0") @QueryParam("offset") int offset, @DefaultValue("5") @QueryParam("limit") int limit) {
        return this.userService.listAllUsers(offset, limit);
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Login login) {
        login.validate();
        Map<String, String> response = new HashMap<>();

        String jwt = this.userService.login(login.getEmail(), login.getPassword());
        if (jwt == null) {
            response.put("message", "These credentials do not match our records");
            return Response.status(422, "Unprocessable Entity").entity(response).build();
        }

        response.put("jwt", jwt);

        return Response.ok(response).build();
    }

}
