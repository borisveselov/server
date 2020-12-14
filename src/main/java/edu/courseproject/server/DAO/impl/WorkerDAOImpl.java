package edu.courseproject.server.DAO.impl;

import edu.courseproject.server.DAO.WorkerDAO;
import edu.courseproject.server.datasource.DataSource;
import edu.courseproject.server.entity.Worker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class WorkerDAOImpl implements WorkerDAO {
    private DataSource datasource = DataSource.getInstance();

   private static final String FIND_ALL_WORKERS="SELECT worker.idworker, " +
           "worker.surname, " +
           "worker.name, " +
           "user.status as status," +
           "worker.seniority, " +
           "worker.phone, " +
           "worker.address, " +
           "worker.user_iduser as userid " +
           "from worker " +
           "join user on user.iduser = worker.user_iduser ";

    private final static String UPDATE_WORKER =
            "update worker set " +
                    "surname = ?," +
                    " name = ?," +
                    " seniority = ?," +
                    " phone = ?," +
                    " address = ?" +
                    " where idworker = ?";
    private final static String FIND_USER_ID="select worker.user_iduser from worker where idworker = ?";

    private final static String INSERT_WORKER="insert into worker values (default, ?, ?, ?, ?, ?, ?)";
    @Override
    public Worker findStatusByUserId(Long idUser) {
        return null;
    }

//    @Override
//    public boolean create(Worker worker, Long idregion) {
//        PreparedStatement preparedStatement = null;
//        try {
//            preparedStatement = datasource.getConnection().prepareStatement(INSERT_WORKER);
//            preparedStatement.setLong(1,worker.getIdUser());
//            preparedStatement.setString(2,worker.getSurname());
//            preparedStatement.setString(3,worker.getName());
//            preparedStatement.setDouble(4,worker.getSeniority());
//            preparedStatement.setString(5,worker.getPhone());
//            preparedStatement.setLong(6, idregion);
//            preparedStatement.executeUpdate();
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    @Override
    public long findIdUserByIdWorker(Long idWorker) {
        try {
            PreparedStatement preparedStatement = datasource.getConnection().prepareStatement(FIND_USER_ID);
            preparedStatement.setLong(1, idWorker);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("user_iduser");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public List<Worker> findAll() {
        List<Worker> workers = new ArrayList<>();
        try {
            Statement statement = datasource.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL_WORKERS);
            while (resultSet.next()){
                Worker worker  = new Worker(resultSet);
                workers.add(worker);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workers;
    }

    @Override
    public boolean delete(Worker worker) {
        return false;
    }

    @Override
    public long create(Worker worker) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = datasource.getConnection().prepareStatement(INSERT_WORKER);
            preparedStatement.setLong(1,worker.getIdUser());
            preparedStatement.setString(2,worker.getSurname());
            preparedStatement.setString(3,worker.getName());
            preparedStatement.setDouble(4,worker.getSeniority());
            preparedStatement.setString(5,worker.getPhone());
            preparedStatement.setString(6, worker.getRegionWorker());
            preparedStatement.executeUpdate();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Worker update(Worker worker) {
        try {
            PreparedStatement preparedStatement = datasource.getConnection().prepareStatement(UPDATE_WORKER);
            preparedStatement.setString(1, worker.getSurname());
            preparedStatement.setString(2, worker.getName());
            preparedStatement.setDouble(3,worker.getSeniority());
            preparedStatement.setString(4,worker.getPhone());
            preparedStatement.setString(5,worker.getRegionWorker());
            preparedStatement.setLong(6, worker.getIdWorker());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return worker;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}
