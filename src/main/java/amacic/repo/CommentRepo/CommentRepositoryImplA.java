package amacic.repo.CommentRepo;

import amacic.data.Comment;
import amacic.exceptions.NotFoundException;
import amacic.exceptions.UnknownException;
import amacic.repo.APostgreSql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentRepositoryImplA extends APostgreSql implements CommentRepository {

    @Override
    public Comment addComment(Comment comment) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM post WHERE id = ?");
            preparedStatement.setLong(1, comment.getPost().getId());

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String[] generatedColumns = {"id"};

                preparedStatement = connection.prepareStatement("INSERT INTO comment(author, text, created_at, post_id) " +
                        "VALUES (?, ?, ?, ?)\n" +
                        "RETURNING *", generatedColumns);
                preparedStatement.setString(1, comment.getAuthor());
                preparedStatement.setString(2, comment.getText());
                preparedStatement.setLong(3, System.currentTimeMillis());
                preparedStatement.setLong(4, comment.getPost().getId());

                preparedStatement.executeUpdate();
                resultSet = preparedStatement.getGeneratedKeys();

                if (resultSet.next()) {
                    comment.setId(resultSet.getLong("id"));
                    comment.setCreatedAt(resultSet.getLong("created_at"));
                }
            } else {
                throw new NotFoundException("Post doesn't exist");
            }

        } catch (SQLException e) {
            throw new UnknownException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return comment;
    }

    @Override
    public Comment editComment(Comment comment) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("select exists(select from post where id = ?)");
            preparedStatement.setLong(1, comment.getPost().getId());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getBoolean("exists")) {

                preparedStatement = connection.prepareStatement("UPDATE comment SET author = ?, text = ? WHERE id = ? ");
                preparedStatement.setString(1, comment.getAuthor());
                preparedStatement.setString(2, comment.getText());
                preparedStatement.setLong(3, comment.getId());

                preparedStatement.executeUpdate();
            } else {
                throw new NotFoundException("Post with id '" + comment.getPost().getId() + "' dos not exist");
            }
        } catch (Exception e) {
            throw new UnknownException();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
        return comment;
    }

    @Override
    public List<Comment> listCommentsByPostId(long postId, int offset, int limit) {
        List<Comment> comments = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM comment WHERE post_id = ? OFFSET ? LIMIT ?");
            preparedStatement.setLong(1, postId);
            preparedStatement.setInt(2, offset);
            preparedStatement.setInt(3, limit);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                comments.add(new Comment(resultSet.getLong("id"), resultSet.getString("author"), resultSet.getString("text"), resultSet.getLong("created_at"), null));
            }

        } catch (Exception e) {
            throw new UnknownException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return comments;
    }

    @Override
    public void deleteComment(long commentId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("DELETE FROM comment where id = ?");
            preparedStatement.setLong(1, commentId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new UnknownException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
    }
}

