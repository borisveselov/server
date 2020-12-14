package edu.courseproject.server.DAO.impl;

import edu.courseproject.server.DAO.SkladDAO;
import edu.courseproject.server.datasource.DataSource;
import edu.courseproject.server.entity.Sklad;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SkladDAOImpl implements SkladDAO {
    private DataSource datasource = DataSource.getInstance();
    private final static String FIND_BY_ID ="select * from logistics.sklad where idsklad in (select logistics.order.sklad_idsklad from logistics.order where logistics.order.idorder = ?)";

    @Override
    public Sklad findByID(Long idOrder) {
        Sklad sklad;
        try {
            PreparedStatement preparedStatement = datasource.getConnection().prepareStatement(FIND_BY_ID);
            preparedStatement.setLong(1, idOrder);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                sklad = new Sklad(resultSet);
                return sklad;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Sklad> findAll() {
        return null;
    }

    @Override
    public boolean delete(Sklad sklad) {
        return false;
    }

    @Override
    public long create(Sklad sklad) throws SQLException {
        return 0;
    }

    @Override
    public Sklad update(Sklad sklad) {
        return null;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}
