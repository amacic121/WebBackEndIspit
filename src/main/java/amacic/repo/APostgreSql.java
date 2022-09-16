package amacic.repo;

import amacic.exceptions.UnknownException;

import java.sql.*;
import java.util.Optional;

abstract public class APostgreSql {

    public APostgreSql() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new UnknownException();
        }
    }

    protected Connection newConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://" + this.getHost() + ":" + this.getPort() + "/" + this.getDatabaseName(), this.getUsername(), this.getPassword()
        );
    }

    protected String getHost() {
        return "localhost";
    }

    protected int getPort() {
        return 5432;
    }

    protected String getDatabaseName() {
        return "web";
    }

    protected String getUsername() {
        return "postgres";
    }

    protected String getPassword() {
        return "postgres";
    }

    protected void closeStatement(Statement statement) {
        try {
            Optional.of(statement).get().close();
        } catch (SQLException throwable) {
            throw new UnknownException();
        }
    }

    protected void closeResultSet(ResultSet resultSet) {
        try {
            Optional.of(resultSet).get().close();
        } catch (SQLException throwable) {
            throw new UnknownException();
        }
    }

    protected void closeConnection(Connection connection) {
        try {
            Optional.of(connection).get().close();
        } catch (SQLException throwable) {
            throw new UnknownException();
        }
    }
}
