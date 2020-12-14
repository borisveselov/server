package edu.courseproject.server.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer extends User {
    //TODO validation
    private long idCustomer;
    private String name;
    private String representative;
    private String email;
    private String regionCustomer;

    public Customer() {
    }

    public Customer(ResultSet resultSet){
        try {
            this.idCustomer = resultSet.getLong("idcustomer");
            this.name = resultSet.getString("name");
            this.representative =resultSet.getString("representative");
            this.setStatus(resultSet.getString("status"));
            this.email =resultSet.getString("email");
            this.regionCustomer = resultSet.getString("address");
            this.setIdUser(resultSet.getLong("userid"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public long getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(long idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegionCustomer() {
        return regionCustomer;
    }

    public void setRegionCustomer(String regionCustomer) {
        this.regionCustomer = regionCustomer;
    }
}
