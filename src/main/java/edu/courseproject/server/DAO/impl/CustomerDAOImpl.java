package edu.courseproject.server.DAO.impl;

import edu.courseproject.server.DAO.CustomerDAO;
import edu.courseproject.server.datasource.DataSource;
import edu.courseproject.server.entity.Customer;
import edu.courseproject.server.entity.Sklad;
import edu.courseproject.server.entity.Worker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {

    private final static String FIND_BY_IDORDER ="select * from logistics.customer where idcustomer in (select logistics.order.customer_idcustomer from logistics.order where logistics.order.idorder = ?)";

    private static final String FIND_ALL_CUSTOMERS="SELECT idcustomer, " +
            "name, " +
            "representative, " +
            "user.status as status," +
            "email, " +
            "address, " +
            "customer.user_iduser as userid " +
            "from customer " +
            "join user on user.iduser = customer.user_iduser ";

    private final static String UPDATE_CUSTOMER =
            "update customer set " +
                    "name = ?," +
                    " representative = ?," +
                    " email = ?," +
                    " address = ?" +
                    " where idcustomer = ?";

    private final static String INSERT_CUSTOMER="insert into customer values (default, ?, ?, ?, ?, ?)";

    private final static String FIND_ID_CUSTOMER="select idcustomer from customer " +
            " where user_iduser=? ";

    private DataSource datasource = DataSource.getInstance();
    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        try {
            Statement statement = datasource.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL_CUSTOMERS);
            while (resultSet.next()){
                Customer customer  = new Customer(resultSet);
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public boolean delete(Customer customer) {
        return false;
    }

    @Override
    public long create(Customer customer) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = datasource.getConnection().prepareStatement(INSERT_CUSTOMER);
            preparedStatement.setLong(1,customer.getIdUser());
            preparedStatement.setString(2,customer.getName());
            preparedStatement.setString(3,customer.getRepresentative());
            preparedStatement.setString(4,customer.getEmail());
            preparedStatement.setString(5, customer.getRegionCustomer());
            preparedStatement.executeUpdate();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Customer update(Customer customer) {
        try {
            PreparedStatement preparedStatement = datasource.getConnection().prepareStatement(UPDATE_CUSTOMER);
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getRepresentative());
            preparedStatement.setString(3,customer.getEmail());
            preparedStatement.setString(4,customer.getRegionCustomer());
            preparedStatement.setLong(5, customer.getIdCustomer());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public long findIdCustomerByIdUser(Long idUser) {
        try {
            PreparedStatement preparedStatement = datasource.getConnection().prepareStatement(FIND_ID_CUSTOMER);
            preparedStatement.setLong(1, idUser);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("idcustomer");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Customer findInOrder(Long idOrder) {
        Customer customer = new Customer();
        try {
            PreparedStatement preparedStatement = datasource.getConnection().prepareStatement(FIND_BY_IDORDER);
            preparedStatement.setLong(1, idOrder);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                customer.setIdCustomer(resultSet.getLong("idcustomer"));
                customer.setIdUser(resultSet.getLong("user_iduser"));
                if(resultSet.getString("name") != null){
                    customer.setName(resultSet.getString("name"));
                }else {
                    customer.setName("");
                }
                customer.setRepresentative(resultSet.getString("representative"));
                customer.setEmail(resultSet.getString("email"));
                customer.setRegionCustomer(resultSet.getString("address"));
                return customer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
