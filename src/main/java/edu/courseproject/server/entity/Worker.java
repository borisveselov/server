package edu.courseproject.server.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Worker extends User {
    private long idWorker;
    private String surname;
    private String name;
    private double seniority;
    private String phone;
    private String regionWorker;

    public Worker() {
    }

    public Worker(long idWorker, String surname, String name, int seniority, String phone, String regionWorker) {
        this.idWorker = idWorker;
        this.surname = surname;
        this.name = name;
        this.seniority = seniority;
        this.phone = phone;
        this.regionWorker = regionWorker;
    }

    public Worker(ResultSet resultSet){
        try {
            this.idWorker = resultSet.getLong("idworker");
            this.surname= resultSet.getString("surname");
            this.name=resultSet.getString("name");
            this.setStatus(resultSet.getString("status"));
            this.seniority=resultSet.getDouble("seniority");
            this.phone=resultSet.getString("phone");
            this.regionWorker = resultSet.getString("address");
            this.setIdUser(resultSet.getLong("userid"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long getIdWorker() {
        return idWorker;
    }

    public void setIdWorker(long idWorker) {
        this.idWorker = idWorker;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSeniority() {
        return seniority;
    }

    public void setSeniority(int seniority) {
        this.seniority = seniority;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegionWorker() {
        return regionWorker;
    }

    public void setRegionWorker(String regionWorker) {
        this.regionWorker = regionWorker;
    }
}
