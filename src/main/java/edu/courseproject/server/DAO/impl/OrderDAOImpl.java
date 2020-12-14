package edu.courseproject.server.DAO.impl;

import edu.courseproject.server.DAO.OrderDAO;
import edu.courseproject.server.datasource.DataSource;
import edu.courseproject.server.entity.Order;
import edu.courseproject.server.entity.Worker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    private DataSource datasource = DataSource.getInstance();

    private final static String FIND_POPULAR ="select logistics.customer.name, logistics.customer.representative , sum(logistics.order.cost) as sum " +
            "from logistics.order " +
            "join logistics.customer on logistics.order.customer_idcustomer = logistics.customer.idcustomer " +
            "where logistics.order.processing <> 'rejected' " +
            "group by logistics.customer.name, logistics.customer.representative";

    private final static String INSERT_ORDER="insert into logistics.order values (default, ?, ?, ?, ?, ?, ?)";

    private static final String FIND_POSTED_ORDERS="SELECT logistics.order.idorder, customer.name, " +
            "customer.representative, " +
            "orderdate " +
            "from logistics.order " +
            "join customer on customer_idcustomer = customer.idcustomer " +
            "where processing='posted' ";
    private final static String UPDATE_ORDER =
            "update logistics.order set " +
                    " processing = ? " +
                    " where logistics.order.idorder = ?";

    private final static String FIND_BY_ID="select * from logistics.order " +
            "where logistics.order.customer_idcustomer in ( select idcustomer from customer  where user_iduser=?)";
    private static final String FIND_PROCESSED_ORDERS="SELECT logistics.order.idorder, customer.name, " +
            "customer.representative, " +
            "orderdate " +
            "from logistics.order " +
            "join customer on customer_idcustomer = customer.idcustomer " +
            "where processing='processed' ";

    private final static String FIND_IDSKLAD ="select logistics.order.sklad_idsklad from logistics.order where idorder = ?";
    @Override
    public List<String> findPostedOrders() {
        List<String> newOrders = new ArrayList<>();
        try {
            Statement statement = datasource.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_POSTED_ORDERS);
            while (resultSet.next()){
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(resultSet.getLong("idorder")).append(")");
                if(resultSet.getString("name")==null){
                    stringBuilder.append("");
                }else {
                    stringBuilder.append(resultSet.getString("name")).append(" - ");
                }
                stringBuilder.append(resultSet.getString("representative")).append(" - ").append(resultSet.getString("orderdate"));
                newOrders.add(stringBuilder.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newOrders;
    }

    @Override
    public List<Order> findSumCost() {
        List<Order> orders = new ArrayList<>();
        try {
            Statement statement = datasource.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_POPULAR);
            while (resultSet.next()){
                Order order = new Order();
                order.setOrderDate(resultSet.getString("name"));
                order.setDeliveryDate(resultSet.getString("representative"));
                order.setCost(resultSet.getDouble("sum"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public List<Order> findByID(Long idUser) {
        List<Order> orders = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = datasource.getConnection().prepareStatement(FIND_BY_ID);
            preparedStatement.setLong(1,idUser);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Order order = new Order(resultSet);
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public List<String> findProcessedOrders() {
        List<String> newOrders = new ArrayList<>();
        try {
            Statement statement = datasource.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_PROCESSED_ORDERS);
            while (resultSet.next()){
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(resultSet.getLong("idorder")).append(")");
                if(resultSet.getString("name")==null){
                    stringBuilder.append("");
                }else {
                    stringBuilder.append(resultSet.getString("name")).append(" - ");
                }
                stringBuilder.append(resultSet.getString("representative")).append(" - ").append(resultSet.getString("orderdate"));
                newOrders.add(stringBuilder.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newOrders;
    }

    @Override
    public Long findIDSklad(Long idOrder) {
        try {
            PreparedStatement preparedStatement = datasource.getConnection().prepareStatement(FIND_IDSKLAD);
            preparedStatement.setLong(1, idOrder);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("sklad_idsklad");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public boolean delete(Order order) {
        return false;
    }

    @Override
    public long create(Order order) throws SQLException {
        long autoIncKey = -1;
        PreparedStatement preparedStatement = datasource.getConnection().prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setLong(1,order.getIdSklad());
        preparedStatement.setLong(2,order.getIdCustomer());
        preparedStatement.setString(3, order.getOrderDate());
        preparedStatement.setString(4, order.getDeliveryDate());
        preparedStatement.setString(5, order.getProcessing());
        preparedStatement.setDouble(6, order.getCost());
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if(resultSet.next()){
            autoIncKey=resultSet.getLong(1);
        }else{
            throw new SQLException("error in getting idOrder");
        }
        return autoIncKey;
    }

    @Override
    public Order update(Order order) {
        try {
            PreparedStatement preparedStatement = datasource.getConnection().prepareStatement(UPDATE_ORDER);
            preparedStatement.setString(1, order.getProcessing());
            preparedStatement.setLong(2, order.getIdOrder());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}
