package edu.courseproject.server.datasource;

import edu.courseproject.server.DAO.*;
import edu.courseproject.server.DAO.impl.*;
import edu.courseproject.server.entity.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;import com.mysql.cj.jdbc.Driver;

public class DataSource {
    private static DataSource instance;
    private volatile static boolean instanceCreated = false;
    private Connection connection;
    private UserDAO userDAO;
    private WorkerDAO workerDAO;
    private CustomerDAO customerDAO;
    private ProductDAO productDAO;
    private OrderDAO orderDAO;
    private ProductOrderDAO productOrderDAO;
    private SkladProductDAO skladProductDAO;


    private DataSource() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String connectionURL = "jdbc:mysql://localhost:3306/logistics?serverTimezone=UTC";
            connection =  DriverManager.getConnection(connectionURL, "root","12345");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DataSource getInstance() {
        if (!instanceCreated) {
            synchronized (DataSource.class) {
                if (!instanceCreated) {
                    instance = new DataSource();
                    instanceCreated = true;
                }
            }
        }
        return instance;
    }

    public UserDAO getUserDAO() {
        if(userDAO == null) {
            userDAO = new UserDAOImpl();
        }
        return userDAO;
    }

    public WorkerDAO getWorkerDAO() {
        if(workerDAO == null) {
            workerDAO = new WorkerDAOImpl();
        }
        return workerDAO;
    }

    public CustomerDAO getCustomerDAO() {
        if(customerDAO == null) {
            customerDAO = new CustomerDAOImpl();
        }
        return customerDAO;
    }

    public ProductDAO getProductDAO() {
        if(productDAO == null) {
            productDAO = new ProductDAOImpl();
        }
        return productDAO;
    }

    public OrderDAO getOrderDAO() {
        if(orderDAO == null) {
            orderDAO = new OrderDAOImpl();
        }
        return orderDAO;
    }

    public ProductOrderDAO getProductOrderDAO() {
        if(productOrderDAO == null) {
            productOrderDAO = new ProductOrderDAOImpl();
        }
        return productOrderDAO;
    }

    public SkladProductDAO getSkladProductDAO() {
        if(skladProductDAO == null) {
            skladProductDAO = new SkladProductDAOImpl();
        }
        return skladProductDAO;
    }

}
