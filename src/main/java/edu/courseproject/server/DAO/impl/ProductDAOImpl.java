package edu.courseproject.server.DAO.impl;

import edu.courseproject.server.DAO.ProductDAO;
import edu.courseproject.server.datasource.DataSource;
import edu.courseproject.server.entity.Product;
import edu.courseproject.server.entity.Worker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {
    private DataSource datasource = DataSource.getInstance();
    private final static String FIND_ALL_PRODUCTS = "select product.idproduct, product.name, product.category, product.measurement, product.price " +
            "from product";
    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        try {
            Statement statement = datasource.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL_PRODUCTS);
            while (resultSet.next()){
                Product product  = new Product(resultSet);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public boolean delete(Product product) {
        return false;
    }

    @Override
    public long create(Product product) throws SQLException {
        return 0;
    }

    @Override
    public Product update(Product product) {
        return null;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}
