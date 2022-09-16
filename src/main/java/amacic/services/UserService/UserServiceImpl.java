package amacic.services.UserService;

import amacic.data.User;
import amacic.exceptions.ValidationException;
import amacic.repo.UserRepo.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.codec.digest.DigestUtils;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class UserServiceImpl implements UserService {

    @Inject
    private UserRepository userRepository;

    @Override
    public User addUser(User user) {
        user.validate();
        user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
        return this.userRepository.addUser(user);
    }

    @Override
    public User editUser(User user) {
        user.validate();
        if (user.getId() == 0){
            throw new ValidationException("User ID is incorrect");
        }
        return this.userRepository.editUser(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return this.userRepository.findUserByEmail(email);
    }

    @Override
    public List<User> listAllUsers(int offset, int limit) {
        return this.userRepository.listAllUsers(offset, limit);
    }

    @Override
    public String login(String email, String password) {

        String hashedPassword = DigestUtils.sha256Hex(password);

        User user = this.userRepository.findUserByEmail(email);
        if (user == null || !user.getPassword().equals(hashedPassword)) {
            return null;
        }

        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + 24 * 60 * 60 * 1000); // One day

        //Secret should be stored outside of project
        Algorithm algorithm = Algorithm.HMAC256("secret");

        return JWT.create()
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withSubject(email)
                .withClaim("role", user.getUserType())
                .sign(algorithm);
    }
}
