package edu.courseproject.server.DAO.impl;

import edu.courseproject.server.DAO.SkladProductDAO;
import edu.courseproject.server.datasource.DataSource;
import edu.courseproject.server.entity.Worker;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class SkladProductDAOImpl implements SkladProductDAO {
    private DataSource datasource = DataSource.getInstance();
    private final static String FIND_SUPPLIERS_IN_SKLAD="select logistics.sklad_product.product_idproduct as idproduct , SUM(logistics.sklad_product.quantity) as amount " +
            "from logistics.sklad_product "  +
            "where logistics.sklad_product.sklad_idsklad in (select logistics.order.sklad_idsklad from logistics.order where logistics.order.idorder = ?) " +
            "group by logistics.sklad_product.product_idproduct " +
            "order by logistics.sklad_product.product_idproduct asc";

    private static final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    @Override
    public Map<Long, Integer> findSuppliersByIdSklad(Long idSkald) {
        Map<Long, Integer> map = new HashMap<>();
        try {
            PreparedStatement preparedStatement = datasource.getConnection().prepareStatement(FIND_SUPPLIERS_IN_SKLAD);
            preparedStatement.setLong(1, idSkald);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
               map.put(resultSet.getLong("idproduct"), resultSet.getInt("amount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean removeFromSklad(Map<Long, Integer> map, Long idSklad) {
        Date date = new Date();
        String removeDate = formatter.format(date);
        Connection connection = datasource.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            Set<Map.Entry<Long,Integer>> demoEntrySet = map.entrySet();
            ArrayList<Map.Entry<Long,Integer>> demoList = new ArrayList<>(demoEntrySet);
            for (Map.Entry<Long,Integer> entry: demoList ) {
                String query = "insert into sklad_product values(default , ' "+idSklad+"','"+entry.getKey()+" ',' "+(-1)*entry.getValue()+" ',' "+removeDate+" ')";
                statement.addBatch(query);
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean fillSklad(Map<Long, Integer> map) {
        Date date = new Date();
        String removeDate = formatter.format(date);
        Connection connection = datasource.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            Set<Map.Entry<Long,Integer>> demoEntrySet = map.entrySet();
            ArrayList<Map.Entry<Long,Integer>> demoList = new ArrayList<>(demoEntrySet);
            for (Map.Entry<Long,Integer> entry: demoList ) {
                String query = "insert into sklad_product values(default , ' "+3+"','"+entry.getKey()+" ',' "+entry.getValue()+" ',' "+removeDate+" ')";
                statement.addBatch(query);
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List findAll() {
        return null;
    }

    @Override
    public boolean delete(Object o) {
        return false;
    }

    @Override
    public long create(Object o) throws SQLException {
        return 0;
    }

    @Override
    public Object update(Object o) {
        return null;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}
