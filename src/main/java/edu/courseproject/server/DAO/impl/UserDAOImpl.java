package edu.courseproject.server.DAO.impl;

import edu.courseproject.server.DAO.UserDAO;
import edu.courseproject.server.datasource.DataSource;
import edu.courseproject.server.entity.User;
import edu.courseproject.server.exception.DAOException;

import java.sql.*;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private DataSource datasource = DataSource.getInstance();
    private static final String FIND_USER_BY_LOGIN_PASSWORD =
            "SELECT * FROM user WHERE login=? AND password=?";

    private final static String USER_UPDATE="update user set status = ? where iduser = ?";

    private final static String INSERT_USER = "insert into user values (default , ?,?,?,?)";

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public boolean delete(User user) {
        return false;
    }

    @Override
    public long create(User user) throws SQLException {
        long autoIncKey = -1;
        PreparedStatement preparedStatement = datasource.getConnection().prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1,user.getLogin());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3,user.getRole());
        preparedStatement.setString(4,user.getStatus());
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if(resultSet.next()){
            autoIncKey=resultSet.getLong(1);
        }else{
            throw new SQLException("error in getting idUser");
        }
        return autoIncKey;
//        Statement stmt = null;
//        Connection con = null;
//        try {
//            con = datasource.getConnection();
//            stmt = con.createStatement(
//                    ResultSet.TYPE_SCROLL_SENSITIVE,
//                    ResultSet.CONCUR_UPDATABLE);
//
//            ResultSet resultSet = stmt.executeQuery(INSERT_WORKER);
//
//            resultSet.moveToInsertRow();
//            resultSet.updateString("login", user.getLogin());
//            resultSet.updateString("password", user.getPassword());
//            resultSet.updateString("role", user.getRole());
//            resultSet.updateString("status", user.getStatus());
//            resultSet.insertRow();
//            resultSet.beforeFirst();
//            return true;
//        } catch (SQLException e ) {
//            e.printStackTrace();
//        } finally {
//            if (stmt != null) {stmt.close();  }
//        }
//        return false;
    }

    @Override
    public User update(User user) {
        try {
            PreparedStatement preparedStatement = datasource.getConnection().prepareStatement(USER_UPDATE);
            preparedStatement.setString(1, user.getStatus());
            preparedStatement.setLong(2, user.getIdUser());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public User findUserByLoginPassword(String login, String password) throws DAOException{
        Connection connection=null;
        PreparedStatement statement=null;
        try {
            connection = datasource.getConnection();
            statement = connection.prepareStatement(FIND_USER_BY_LOGIN_PASSWORD);
            statement.setString(1,login);
            statement.setString(2,password);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return new User(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException("Request or table failed ", e);
        }
//        finally {
//            close(statement);
//            close(connection);
//        }
        return null;
    }

}
