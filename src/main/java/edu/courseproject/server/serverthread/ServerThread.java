package edu.courseproject.server.serverthread;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.courseproject.server.DAO.*;
import edu.courseproject.server.DAO.impl.SkladDAOImpl;
import edu.courseproject.server.DAO.impl.SkladProductDAOImpl;
import edu.courseproject.server.action.DAOAction;
import edu.courseproject.server.datasource.DataSource;
import edu.courseproject.server.entity.*;
import edu.courseproject.server.exception.DAOException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;

public class ServerThread extends Thread {
    private Socket socket;
    private Gson gson;

    public ServerThread(Socket socket) {
        this.socket = socket;
        gson = new Gson();

    }

    public void handle(JSONObject jsonQuery, PrintWriter writer) throws DAOException, SQLException {
        switch (DAOAction.valueOf(jsonQuery.get("action").toString())){

            case IS_USER_EXISTS: {
                UserDAO userDAO = DataSource.getInstance().getUserDAO();
                User actualUser = gson.fromJson(jsonQuery.getString("user"), User.class);
                User expectedUser = userDAO.findUserByLoginPassword(actualUser.getLogin(), actualUser.getPassword());
                JSONObject jsonObject = new JSONObject();
                if (expectedUser == null) {
                    jsonObject.put("isExists", false);
                    System.out.println("USER_IS_NOT_EXIST");
                } else {
                    jsonObject.put("isExists", true);
                    jsonObject.put("user", gson.toJson(expectedUser));
                    System.out.println("USER_EXIST");
                }
                writer.println(jsonObject.toString());
                break;
            }
            case FIND_ALL_WORKERS:{
                WorkerDAO workerDAO = DataSource.getInstance().getWorkerDAO();
                String workers = gson.toJson(workerDAO.findAll());
                JSONObject object = new JSONObject();
                object.put("workers", workers);
                System.out.println("FIND_ALL_WORKERS");
                writer.println(object.toString());
                break;
            }
            case UPDATE_WORKER:{
                WorkerDAO workerDAO = DataSource.getInstance().getWorkerDAO();
                Worker worker = gson.fromJson(jsonQuery.getString("worker"), Worker.class);
                workerDAO.update(worker);
                UserDAO userDAO = DataSource.getInstance().getUserDAO();
                userDAO.update(worker);
                System.out.println("UPDATE_WORKER");
                break;
            }
            case ADD_WORKER:{
                UserDAO userDAO = DataSource.getInstance().getUserDAO();
                Worker worker = gson.fromJson(jsonQuery.getString("worker"), Worker.class);
                long iduser = userDAO.create(worker);
                worker.setIdUser(iduser);
                WorkerDAO workerDAO = DataSource.getInstance().getWorkerDAO();
                workerDAO.create(worker);
                System.out.println("ADD_WORKER");
                break;
            }
            case FIND_ALL_CUSTOMERS:{
                CustomerDAO customerDAO = DataSource.getInstance().getCustomerDAO();
                String customers = gson.toJson(customerDAO.findAll());
                JSONObject object = new JSONObject();
                object.put("customers", customers);
                writer.println(object.toString());
                System.out.println("FIND_ALL_CUSTOMERS");
                break;
            }
            case UPDATE_CUSTOMER:{
                CustomerDAO customerDAO = DataSource.getInstance().getCustomerDAO();
                Customer customer = gson.fromJson(jsonQuery.getString("customer"), Customer.class);
                customerDAO.update(customer);
                UserDAO userDAO = DataSource.getInstance().getUserDAO();
                userDAO.update(customer);
                System.out.println("UPDATE_CUSTOMER");
                break;
            }
            case ADD_CUSTOMER:{
                UserDAO userDAO = DataSource.getInstance().getUserDAO();
                Customer customer = gson.fromJson(jsonQuery.getString("customer"), Customer.class);
                long iduser = userDAO.create(customer);
                customer.setIdUser(iduser);
                CustomerDAO customerDAO = DataSource.getInstance().getCustomerDAO();
                customerDAO.create(customer);
                System.out.println("ADD_CUSTOMER");
                break;
            }
            case HAS_POSTED_ORDERS:{
                OrderDAO orderDAO = DataSource.getInstance().getOrderDAO();
                String postedOrders = gson.toJson(orderDAO.findPostedOrders());
                JSONObject object = new JSONObject();
                object.put("postedOrders", postedOrders);
                writer.println(object.toString());
                System.out.println("HAS_NEW_POSTED_ORDERS");
                break;
            }

            case FIND_ALL_PRODUCTS:{
                ProductDAO productDAO = DataSource.getInstance().getProductDAO();
                String products = gson.toJson(productDAO.findAll());
                JSONObject object = new JSONObject();
                object.put("products", products);
                writer.println(object.toString());
                System.out.println("FIND_ALL_PRODUCTS");
                break;
            }
            case ADD_ORDER:{
                OrderDAO orderDAO = DataSource.getInstance().getOrderDAO();
                Order order = gson.fromJson(jsonQuery.getString("order"), Order.class);
                CustomerDAO customerDAO = DataSource.getInstance().getCustomerDAO();
                long idCustomer = customerDAO.findIdCustomerByIdUser(order.getIdCustomer());
                order.setIdCustomer(idCustomer);
                long idOrder = orderDAO.create(order);
                Type productMap = new TypeToken<HashMap<Long, Integer>>() {}.getType();
                Map<Long,Integer> newMap = gson.fromJson(jsonQuery.getString("map"), productMap);
                ProductOrderDAO productOrderDAO = DataSource.getInstance().getProductOrderDAO();
                productOrderDAO.create(idOrder, newMap);
                System.out.println("ADD_ORDER");
                break;
            }
            case REPORT:{
                long idOreder = jsonQuery.getLong("idorder");
                SkladDAO skladDAO = new SkladDAOImpl();
                Sklad sklad = skladDAO.findByID(idOreder);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("sklad", gson.toJson(sklad));
                CustomerDAO customerDAO = DataSource.getInstance().getCustomerDAO();
                Customer customer = customerDAO.findInOrder(idOreder);
                jsonObject.put("customer", gson.toJson(customer));
                ProductOrderDAO productOrderDAO = DataSource.getInstance().getProductOrderDAO();
                String products = gson.toJson(productOrderDAO.findFullInfo(idOreder));
                jsonObject.put("products", products);
                System.out.println("GET DATA FROM REPORT");
                writer.println(jsonObject.toString());
                break;
            }

            case CHECK_ORDER:{
                long idOreder = jsonQuery.getLong("idorder");
                SkladProductDAO skladProductDAO = DataSource.getInstance().getSkladProductDAO();
                Map<Long, Integer> mapInsklad = skladProductDAO.findSuppliersByIdSklad(idOreder);
                ProductOrderDAO productOrderDAO = DataSource.getInstance().getProductOrderDAO();
                Map<Long, Integer> mapInOrder = productOrderDAO.findProductsInOrder(idOreder);
                JSONObject jsonObject = new JSONObject();
                if (equalsMap(mapInsklad, mapInOrder)) {
                    jsonObject.put("checkOrder", true);
                    System.out.println("ORDER IS VALID");
                } else {
                    jsonObject.put("checkOrder", false);
                    System.out.println("INVALID ORDER");
                }
                writer.println(jsonObject.toString());
                break;
            }
            case UPDATE_ORDER:{
                OrderDAO orderDAO = DataSource.getInstance().getOrderDAO();
                Order order = gson.fromJson(jsonQuery.getString("order"), Order.class);
                orderDAO.update(order);
                System.out.println("UPDATE_ORDER");
                break;
            }
            case TOP_CUSTOMER:{
                OrderDAO orderDAO = DataSource.getInstance().getOrderDAO();
                String orders = gson.toJson(orderDAO.findSumCost());
                JSONObject object = new JSONObject();
                object.put("sum", orders);
                System.out.println("GET_STATISTICS_OF_CUSTOMER'S_TOTAL_COST");
                writer.println(object.toString());
                break;
            }
            case TOP_PRODUCT:{
                ProductOrderDAO productOrderDAO = DataSource.getInstance().getProductOrderDAO();
                String products = gson.toJson(productOrderDAO.findPopularProduct());
                JSONObject object = new JSONObject();
                object.put("popular", products);
                System.out.println("GET_5_TOP_PRODUCT");
                writer.println(object.toString());
                break;
            }
            case HISTORY_CUSTOMER:{
                OrderDAO orderDAO = DataSource.getInstance().getOrderDAO();
                long idUser = jsonQuery.getLong("idcustomer");
                String orders = gson.toJson(orderDAO.findByID(idUser));
                JSONObject object = new JSONObject();
                object.put("orders", orders);
                System.out.println("GET_CUSTOMERS'S_HISTORY");
                writer.println(object.toString());
                break;

            }
            case HAS_PROCESSED_ORDERS:{
                OrderDAO orderDAO = DataSource.getInstance().getOrderDAO();
                String postedOrders = gson.toJson(orderDAO.findProcessedOrders());
                JSONObject object = new JSONObject();
                object.put("processedOrders", postedOrders);
                writer.println(object.toString());
                System.out.println("MARK_ORDER_AS_PROCESSED");
                break;
            }
            case MOVE_FROM_SKLAD:{
                long idOrder = jsonQuery.getLong("idorder");
                ProductOrderDAO productOrderDAO = DataSource.getInstance().getProductOrderDAO();
                Map<Long, Integer> mapInOrder = productOrderDAO.findProductsInOrder(idOrder);
                OrderDAO orderDAO = DataSource.getInstance().getOrderDAO();
                long idSklad = orderDAO.findIDSklad(idOrder);
                SkladProductDAO skladProductDAO = new SkladProductDAOImpl();
                skladProductDAO.removeFromSklad(mapInOrder, idSklad);
                System.out.println("DELIVERY FROM SKLAD");
                break;
            }
            case FILL_SKLAD:{
                Type productMap = new TypeToken<HashMap<Long, Integer>>() {}.getType();
                Map<Long,Integer> newMap = gson.fromJson(jsonQuery.getString("map"), productMap);
                SkladProductDAO skladProductDAO = new SkladProductDAOImpl();
                skladProductDAO.fillSklad(newMap);
                System.out.println("FILL_SKLAD");
                break;
            }
        }
    }
    @Override
    public void run() {

            try(BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(),true)) {
                while(!socket.isClosed()) {
                    JSONObject jsonQuery = new JSONObject(reader.readLine());
                    handle(jsonQuery, writer);
                }
            } catch (IOException | DAOException | SQLException e) {
                System.out.println("client disconnect");
            }
    }

    public boolean equalsMap( Map<Long, Integer> mapInsklad,  Map<Long, Integer> mapInOrder){
        Set<Map.Entry<Long,Integer>> demoOrderSet = mapInOrder.entrySet();
        ArrayList<Map.Entry<Long,Integer>> demoOrderList = new ArrayList<>(demoOrderSet);
        for (Map.Entry<Long,Integer> entryOrder: demoOrderList) {
            if(mapInsklad.containsKey(entryOrder.getKey())){
                if(mapInsklad.get(entryOrder.getKey())<entryOrder.getValue()){
                    return false;
                }
            }else {
                return false;
            }
        }
        return true;
    }

}
