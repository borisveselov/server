package edu.courseproject.server.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private long idUser;
    private String login;
    private String password;
    private String role;
    private String status;

    public User() {
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(ResultSet resultSet){
        try {
            this.idUser = resultSet.getLong("iduser");
            this.login = resultSet.getString("login");
            this.password=resultSet.getString("password");
            this.role=resultSet.getString("role");
            this.status=resultSet.getString("status");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
