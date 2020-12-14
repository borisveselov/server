package edu.courseproject.server.DAO;

import edu.courseproject.server.DAO.BaseDAO;
import edu.courseproject.server.entity.User;
import edu.courseproject.server.exception.DAOException;

import java.sql.SQLException;

public interface UserDAO extends BaseDAO<User> {
    User findUserByLoginPassword(String login, String password) throws DAOException;
}
