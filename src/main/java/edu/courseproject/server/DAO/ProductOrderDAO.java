package edu.courseproject.server.DAO;

import edu.courseproject.server.entity.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ProductOrderDAO extends BaseDAO {
    boolean create (Long idOrder, Map<Long,Integer> map) throws SQLException;
    Map<Long, Integer> findProductsInOrder(Long idOrder);
    List<String> findFullInfo(Long idOrder);
    List<Product> findPopularProduct();
}
