package amacic.repo.CategoryRepo;

import amacic.data.Post;
import amacic.exceptions.NotFoundException;
import amacic.exceptions.ObjectExistsException;
import amacic.exceptions.UnknownException;
import amacic.exceptions.ValidationException;
import amacic.data.Category;
import amacic.repo.APostgreSql;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryRepositoryImplA extends APostgreSql implements CategoryRepository {

    @Override
    public Category addCategory(Category category) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT EXISTS(SELECT FROM category WHERE name = ?)");
            preparedStatement.setString(1, category.getName());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && !resultSet.getBoolean("exists")) {
                String[] generatedColumns = {"id"};

                preparedStatement = connection.prepareStatement("INSERT INTO category\n" +
                        "    (name, description)\n" +
                        "SELECT ?, ?\n" +
                        "WHERE\n" +
                        "    NOT EXISTS (SELECT name FROM category WHERE name = ?)", generatedColumns);
                preparedStatement.setString(1, category.getName());
                preparedStatement.setString(2, category.getDescription());
                preparedStatement.setString(3, category.getName());

                preparedStatement.executeUpdate();
                resultSet = preparedStatement.getGeneratedKeys();

                if (resultSet.next()) {
                    category.setId(resultSet.getLong("id"));
                }

            } else if (resultSet.getBoolean("exists")) {
                throw new ObjectExistsException("Category by the name of '" + category.getName() + "' already exists.");
            }
        } catch (SQLException e) {
            throw new UnknownException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return category;
    }

    @Override
    public Category updateCategory(Category category) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int idReturnedByQuery = 0;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("WITH rows AS(UPDATE category SET name = ?, description = ? " +
                    "WHERE id = ? AND NOT EXISTS (SELECT name FROM category WHERE name = ? AND id != ?) RETURNING *) SELECT count(*) FROM rows");
            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());
            preparedStatement.setLong(3, category.getId());
            preparedStatement.setString(4, category.getName());
            preparedStatement.setLong(5, category.getId());

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt("count") != 0) {
                idReturnedByQuery = resultSet.getInt("count");
            }

        } catch (Exception e) {
            throw new UnknownException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        if (idReturnedByQuery == 0) {
            throw new ValidationException("Invalid input, failed to update category");
        }
        return category;
    }

    @Override
    public List<Category> listAllCategories(int offset, int limit) {
        int postId = 6, postTitle = 1, postText = 2, postAuthor = 3, postCreatedAt = 4, postNumberOfVisits = 5;
        List<Category> categories = new ArrayList<>();
        List<Post> posts = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM category OFFSET ? LIMIT ?");
            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, limit);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                categories.add(new Category(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getString("description"), null));
            }

            for (Category category : categories) {
                preparedStatement = connection.prepareStatement("SELECT * FROM post WHERE category_id = ?");
                preparedStatement.setLong(1, category.getId());
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    posts.add(new Post(resultSet.getLong(postId), resultSet.getString(postTitle), resultSet.getString(postText),
                            resultSet.getString(postAuthor), resultSet.getLong(postCreatedAt),
                            resultSet.getLong(postNumberOfVisits), null, null, null));
                }

                List<Post> postsTemp = new ArrayList<>(posts);
                category.setPosts(postsTemp);
                posts.removeAll(posts);
            }

        } catch (Exception e) {
            throw new UnknownException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return categories;
    }


    @Override
    public Category findCategoryByPostId(long postId) {
        long categoryId;
        Category category = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT category_id FROM post WHERE id = ?");
            preparedStatement.setLong(1, postId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                categoryId = resultSet.getLong(1);
            } else {
                throw new NotFoundException("No post with id: " + postId);
            }

            preparedStatement = connection.prepareStatement("SELECT * FROM category WHERE id = ?");
            preparedStatement.setLong(1, categoryId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                category = new Category(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getString("description"), null);
            }

        } catch (SQLException e) {
            throw new UnknownException();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
        return category;
    }

    @Override
    public int getPages() {

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        int rows = 0;
        try {
            connection = this.newConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM category");
            resultSet.next();
            rows = resultSet.getInt("count");


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }


        return rows;
    }


    @Override
    public void deleteCategory(long categoryId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM post WHERE category_id = ?");
            preparedStatement.setLong(1, categoryId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getLong("count") == 0) {
                preparedStatement = connection.prepareStatement("DELETE FROM category where id = ?");
                preparedStatement.setLong(1, categoryId);
                preparedStatement.executeUpdate();

            } else {
                throw new ObjectExistsException("At least one post exists in this category. Delete the post before" +
                        " deleting the category");
            }
        } catch (SQLException e) {
            throw new UnknownException();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
    }
}
