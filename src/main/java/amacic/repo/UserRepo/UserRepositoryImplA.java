package amacic.repo.UserRepo;

import amacic.data.User;
import amacic.exceptions.NotFoundException;
import amacic.exceptions.ObjectExistsException;
import amacic.exceptions.UnknownException;
import amacic.repo.APostgreSql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImplA extends APostgreSql implements UserRepository {
    @Override
    public User addUser(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT EXISTS (SELECT FROM public.user WHERE email = ?)");
            preparedStatement.setString(1, user.getEmail());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && !resultSet.getBoolean("exists")) {
                String[] generatedColumns = {"id"};

                preparedStatement = connection.prepareStatement("INSERT INTO public.user (email, first_name, last_name, user_type, status, password) " +
                        "VALUES(?, ?, ?, ?, ?, ?)", generatedColumns);
                preparedStatement.setString(1, user.getEmail());
                preparedStatement.setString(2, user.getFirstName());
                preparedStatement.setString(3, user.getLastName());
                preparedStatement.setString(4, user.getUserType());
                preparedStatement.setString(5, user.getStatus());
                preparedStatement.setString(6, user.getPassword());

                preparedStatement.executeUpdate();
                resultSet = preparedStatement.getGeneratedKeys();

                if (resultSet.next()) {
                    user.setId(resultSet.getLong("id"));
                }

            } else if (resultSet.getBoolean("exists")) {
                throw new ObjectExistsException("User with email: '" + user.getEmail() + "' already exists.");
            }

        } catch (SQLException e) {
            throw new UnknownException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return user;
    }

    @Override
    public User editUser(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("select exists(select from public.user where id = ?)");
            preparedStatement.setLong(1, user.getId());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getBoolean("exists")) {

                preparedStatement = connection.prepareStatement("UPDATE public.user SET email = ?, first_name = ?, " +
                        "last_name = ?, user_type = ?, status = ?, password = ? WHERE id = 1");
                preparedStatement.setString(1, user.getEmail());
                preparedStatement.setString(2, user.getFirstName());
                preparedStatement.setString(3, user.getLastName());
                preparedStatement.setString(4, user.getUserType());
                preparedStatement.setString(5, user.getStatus());
                preparedStatement.setString(6, user.getPassword());

                preparedStatement.executeUpdate();
            } else {
                throw new ObjectExistsException("User with id: '" + user.getId() + "' doesn't exist.");
            }
        } catch (Exception e) {
            throw new UnknownException();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        User user = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM public.user WHERE email = ?");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User(resultSet.getLong("id"), resultSet.getString("email"),
                        resultSet.getString("first_name"), resultSet.getString("last_name"),
                        resultSet.getString("user_type"), resultSet.getString("status"),
                        resultSet.getString("password"));
            } else {
                throw new NotFoundException("User by the email: '" + email + "' doesn't exist");
            }

            return user;

        } catch (SQLException e) {
            throw new UnknownException();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
    }

    @Override
    public List<User> listAllUsers(int offset, int limit) {
        List<User> users = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM public.user OFFSET ? LIMIT ?");
            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, limit);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                users.add(new User(resultSet.getLong("id"), resultSet.getString("email"),
                        resultSet.getString("first_name"), resultSet.getString("last_name"),
                        resultSet.getString("user_type"), resultSet.getString("status"),
                        resultSet.getString("password")));
            }

        } catch (Exception e) {
            throw new UnknownException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return users;
    }
}
