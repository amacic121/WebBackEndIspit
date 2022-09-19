package amacic.repo.PostRepo;

import amacic.exceptions.NotFoundException;
import amacic.exceptions.UnknownException;
import amacic.data.Category;
import amacic.data.Post;
import amacic.data.Tag;
import amacic.repo.APostgreSql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class PostRepositoryImplA extends APostgreSql implements PostRepository {

    @Override
    public Post addPost(Post post) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT EXISTS(SELECT * FROM category WHERE id = ?)");
            preparedStatement.setLong(1, post.getCategory().getId());

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getBoolean("exists")) {

                String[] generatedColumns = {"id"};

                preparedStatement = connection.prepareStatement("INSERT INTO post(title, text, author, created_at, number_of_visits, category_id)\n" +
                        "VALUES (?, ?, ?, ?, ?, ?)\n" +
                        "RETURNING *;", generatedColumns);
                preparedStatement.setString(1, post.getTitle());
                preparedStatement.setString(2, post.getText());
                preparedStatement.setString(3, post.getAuthor());
                preparedStatement.setLong(4, System.currentTimeMillis());
                preparedStatement.setLong(5, 0);
                preparedStatement.setLong(6, post.getCategory().getId());

                preparedStatement.executeUpdate();
                resultSet = preparedStatement.getGeneratedKeys();

                if (resultSet.next()) {
                    post.setId(resultSet.getLong("id"));
                    post.setCreatedAt(resultSet.getLong("created_at"));
                }
            } else {
                throw new NotFoundException("Category doesn't exist");
            }

        } catch (SQLException e) {
            throw new UnknownException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return post;
    }

    @Override
    public Post updatePost(Post post) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("select exists(select from category where id = ?)");
            preparedStatement.setLong(1, post.getCategory().getId());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getBoolean("exists")) {

                preparedStatement = connection.prepareStatement("UPDATE post SET title = ?, text = ?, author = ?, " +
                        "category_id = ?, number_of_visits = ? WHERE id = ? ");
                preparedStatement.setString(1, post.getTitle());
                preparedStatement.setString(2, post.getText());
                preparedStatement.setString(3, post.getAuthor());
                preparedStatement.setLong(4, post.getCategory().getId());
                preparedStatement.setLong(5, post.getNumberOfVisits());
                preparedStatement.setLong(6, post.getId());

                preparedStatement.executeUpdate();
            } else {
                throw new NotFoundException("Category with id '" + post.getCategory().getId() + "' dos not exist");
            }
        } catch (Exception e) {
            throw new UnknownException();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
        return post;
    }

    @Override
    public List<Post> listAllPosts(int offset, int limit) {
        List<Post> posts = new ArrayList<>();

        int postTitle = 1, postText = 2, postAuthor = 3, postCreatedAt = 4, postNumberOfVisits = 5,
                postId = 6, categoryId = 7, tagId = 8, tagValue = 9, categoryName = 10, categoryDescription = 11;


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
                    "\ton p.category_id = c.id)\n" +
                    "order by p.created_at desc\n" +
                    "OFFSET ? LIMIT ?");
            preparedStatement.setLong(1, offset);
            preparedStatement.setLong(2, limit);
            resultSet = preparedStatement.executeQuery();

            Map<Long, List<String>> postTags = new HashMap<>();

            while (resultSet.next()) {
                long currentPostId = resultSet.getLong("id");

                if (!postTags.containsKey(currentPostId)) {
                    ArrayList<String> listOfTags = new ArrayList<>();
                    listOfTags.add(resultSet.getString(tagValue));

                    postTags.put(currentPostId, listOfTags);

                    Category category = new Category(resultSet.getLong(categoryId), resultSet.getString(categoryName),
                            resultSet.getString(categoryDescription), null);

                    posts.add(new Post(resultSet.getLong(postId), resultSet.getString(postTitle), resultSet.getString(postText),
                            resultSet.getString(postAuthor), resultSet.getLong(postCreatedAt),
                            resultSet.getLong(postNumberOfVisits), category, null, null));
                } else {
                    postTags.get(currentPostId).add(resultSet.getString(tagValue));
                }
            }

            for (Post post : posts) {
                post.setTagsString(String.join(", ", postTags.get(post.getId())));
            }

        } catch (Exception e) {
            throw new UnknownException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return posts;
    }

    @Override
    public List<Post> listPostsByText(int offset, int limit, String text) {
        int postId = 6, postTitle = 1, postText = 2, postAuthor = 3, postCreatedAt = 4, postNumberOfVisits = 5;
        List<Post> posts = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM post WHERE title LIKE ? OR text LIKE ? OFFSET ? LIMIT ?");
            preparedStatement.setString(1, "%" + text + "%");
            preparedStatement.setString(2, "%" + text + "%");
            preparedStatement.setInt(3, offset);
            preparedStatement.setInt(4, limit);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                posts.add(new Post(resultSet.getLong(postId), resultSet.getString(postTitle), resultSet.getString(postText),
                        resultSet.getString(postAuthor), resultSet.getLong(postCreatedAt),
                        resultSet.getLong(postNumberOfVisits), null, null, null));
            }
        } catch (Exception e) {
            throw new UnknownException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return posts;
    }

    @Override
    public List<Post> listPostsByTag(int offset, int limit, String tagValue) {
        int postId = 6, postTitle = 1, postText = 2, postAuthor = 3, postCreatedAt = 4, postNumberOfVisits = 5;
        List<Post> posts = new ArrayList<>();
        Tag tag;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM tag WHERE value = ?");
            preparedStatement.setString(1, tagValue);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                tag = new Tag(resultSet.getLong("id"), resultSet.getString("value"));

                preparedStatement = connection.prepareStatement("SELECT p.* " +
                        "\tFROM(post_tag as pt \n" +
                        "\t\t FULL JOIN post as p \n" +
                        "\t\t on pt.post_id = p.id \n" +
                        "\t\t LEFT JOIN tag as t  \n" +
                        "\t\t on pt.tag_id = t.id)\n" +
                        "\t\t \t\t WHERE pt.tag_id = ?" +
                        "OFFSET ? LIMIT ?");
                preparedStatement.setLong(1, tag.getId());
                preparedStatement.setInt(2, offset);
                preparedStatement.setInt(3, limit);
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    posts.add(new Post(resultSet.getLong(postId), resultSet.getString(postTitle), resultSet.getString(postText),
                            resultSet.getString(postAuthor), resultSet.getLong(postCreatedAt),
                            resultSet.getLong(postNumberOfVisits), null, null, null));
                }
            }else {
                throw new UnknownException("Tag doesn't exist");
            }
        } catch (Exception e) {
            throw new UnknownException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return posts;
    }

    @Override
    public void deletePost(long postId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("DELETE FROM post_tag where post_id = ?");
            preparedStatement.setLong(1, postId);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("DELETE FROM post where id = ?");
            preparedStatement.setLong(1, postId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new UnknownException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
    }

    @Override
    public Post getPostById(long postId) {
        Post post = null;
        int postIdNum = 6, postTitle = 1, postText = 2, postAuthor = 3, postCreatedAt = 4, postNumberOfVisits = 5;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM post WHERE id = ?");
            preparedStatement.setLong(1, postId);
            resultSet = preparedStatement.executeQuery();


            if (resultSet.next()){
                post = new Post(resultSet.getLong(postIdNum), resultSet.getString(postTitle), resultSet.getString(postText),
                        resultSet.getString(postAuthor), resultSet.getLong(postCreatedAt),
                        resultSet.getLong(postNumberOfVisits), null, null, null);
            }else {
                throw new NotFoundException("No post with id: " + postId);
            }

        } catch (Exception e) {
            throw new UnknownException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return post;
    }
}
