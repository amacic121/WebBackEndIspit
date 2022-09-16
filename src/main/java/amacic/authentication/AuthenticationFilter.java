package amacic.authentication;

import amacic.data.User;
import amacic.repo.UserRepo.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    UserRepository userRepository;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        if (isAuthnNotRequired(requestContext)) {
            return;
        }

        try {
            String token = requestContext.getHeaderString("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.replace("Bearer ", "");
            } else {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }

            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);

            if (isAuthorized(jwt) && isContentCreatorAuthRequired(requestContext)) {
                if (isContentCreator(jwt)) {
                    return;
                } else {
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                }
            }

            if (isAuthorized(jwt) && isAdminAuthRequired(requestContext)) {
                if (isAdmin(jwt)) {
                    return;
                } else {
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                }
            }
        } catch (Exception exception) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private boolean isAuthnNotRequired(ContainerRequestContext req) {
        if (req.getUriInfo().getPath().contains("login") || req.getUriInfo().getPath().contains("findCategoryByPost") ||
                req.getUriInfo().getPath().contains("listAllCategories") || req.getUriInfo().getPath().contains("addComment")
                || req.getUriInfo().getPath().contains("listCommentsByPost") || req.getUriInfo().getPath().contains("listAllPosts")
                || req.getUriInfo().getPath().contains("getPostById")) {
            return true;
        }
        return false;
    }

    private boolean isContentCreatorAuthRequired(ContainerRequestContext req) {
        if (req.getUriInfo().getPath().contains("deleteCategory") || req.getUriInfo().getPath().contains("updateCategory") ||
                req.getUriInfo().getPath().contains("addCategory") || req.getUriInfo().getPath().contains("editComment")
                || req.getUriInfo().getPath().contains("deleteComment") || req.getUriInfo().getPath().contains("editPost")
                || req.getUriInfo().getPath().contains("deletePost") || req.getUriInfo().getPath().contains("deletePost")) {
            return true;
        }
        return false;
    }

    private boolean isAdminAuthRequired(ContainerRequestContext req) {
        if (req.getUriInfo().getPath().contains("addUser") || req.getUriInfo().getPath().contains("editUser") ||
                req.getUriInfo().getPath().contains("findUserByEmail") || req.getUriInfo().getPath().contains("listAllUsers")) {
            return true;
        }
        return false;
    }

    public boolean isContentCreator(DecodedJWT jwt) {
        String userType = jwt.getClaim("role").asString();

        if (userType.equals("content_creator") || userType.equals("admin")) {
            return true;
        }
        return false;
    }

    public boolean isAdmin(DecodedJWT jwt) {
        String userType = jwt.getClaim("role").asString();

        if (userType.equals("admin")) {
            return true;
        }

        return false;
    }

    public boolean isAuthorized(DecodedJWT jwt) {
        String email = jwt.getSubject();

        User user = this.userRepository.findUserByEmail(email);

        if (user == null) {
            return false;
        }
        return true;
    }
}

