package amacic.repo.PostTagRepo;

import amacic.exceptions.UnknownException;
import amacic.repo.APostgreSql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostTagRepositoryImplA extends APostgreSql implements PostTagRepository {

    @Override
    public void addPostTag(long post_id, long tag_id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            String[] generatedColumns = {"id"};

            preparedStatement = connection.prepareStatement("INSERT INTO post_tag (post_id, tag_id) " +
                    "VALUES (?, ?)", generatedColumns);
            preparedStatement.setLong(1, post_id);
            preparedStatement.setLong(2, tag_id);

            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

        } catch (SQLException e) {
            throw new UnknownException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
    }

    @Override
    public void removePostFromTag(long postId) {
        Connection connection = null;

        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("DELETE FROM post_tag where post_id = ?");
            preparedStatement.setLong(1, postId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new UnknownException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
    }

}
