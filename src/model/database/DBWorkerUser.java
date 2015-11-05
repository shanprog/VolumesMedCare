package model.database;

import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBWorkerUser {

    private Statement statement = DataBaseConnect.getStatement();
    private ResultSet resultSet;

    public boolean userIsExist(String login) {

        try {
            resultSet = statement.executeQuery(String.format("SELECT login FROM users WHERE login = '%s'", login));
            return resultSet.next();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return false;
    }

    public boolean userIsActual(String login) {
        try {
            resultSet = statement.executeQuery(String.format("SELECT active FROM users WHERE login = '%s'", login));

            while (resultSet.next()) return resultSet.getInt("active") != 0;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return false;
    }

    public char[] getPassword(String login) {
        try {
            resultSet = statement.executeQuery(String.format("SELECT password FROM users WHERE login = '%s'", login));

            while (resultSet.next())
                return resultSet.getString("password").toCharArray();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return null;
    }

    public User getUser(String login) {
        try {

            User user = User.getInstance();
            resultSet = statement.executeQuery(String.format("SELECT name, role FROM users WHERE login = '%s'", login));

            while (resultSet.next()) {
                user.setLogin(login);
                user.setName(resultSet.getString("name"));
                user.setRole(resultSet.getInt("role"));
            }

            return user;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return null;
    }

}
