package amacic.repo.TagRepo;

import amacic.exceptions.UnknownException;
import amacic.data.Tag;
import amacic.repo.APostgreSql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagRepositoryImplA extends APostgreSql implements TagRepository {

    @Override
    public Tag createTagIfNameNotExist(Tag tag) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM tag WHERE VALUE = ?");
            preparedStatement.setString(1, tag.getValue());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getString("value").equals(tag.getValue())) {
                tag.setId(resultSet.getLong("id"));
            } else {
                String[] generatedColumns = {"id"};

                preparedStatement = connection.prepareStatement("INSERT INTO tag (value) SELECT ? WHERE NOT EXISTS" +
                        " (SELECT value FROM tag WHERE value = ?)", generatedColumns);
                preparedStatement.setString(1, tag.getValue());
                preparedStatement.setString(2, tag.getValue());

                preparedStatement.executeUpdate();
                resultSet = preparedStatement.getGeneratedKeys();

                if (resultSet.next()) {
                    tag.setId(resultSet.getLong("id"));
                }
            }
        } catch (SQLException e) {
            throw new UnknownException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return tag;
    }

    @Override
    public List<Tag> findTagsByPostId(long postId) {
        List<Tag> tags = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT p.*, t.*, c.* \n" +
                    "FROM(\n" +
                    "\t\tpost_tag as pt \n" +
                    "\t\tFULL JOIN post as p \n" +
                    "\t\t\ton pt.post_id = p.id \n" +
                    "\t\tLEFT JOIN tag as t \n" +
                    "\t\t\ton pt.tag_id = t.id\n" +
                    "LEFT JOIN category as c \n" +
                    "\ton p.category_id = c.id)\n");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tags.add(new Tag(resultSet.getLong(8), resultSet.getString(9)));
            }
        } catch (Exception e) {
            throw new UnknownException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return tags;
    }
}
