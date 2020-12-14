package edu.courseproject.server.DAO;

import edu.courseproject.server.entity.Customer;

public interface CustomerDAO extends  BaseDAO<Customer> {
        long findIdCustomerByIdUser(Long idUser);
        Customer findInOrder (Long idOrder);
}
