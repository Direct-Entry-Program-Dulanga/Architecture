package lk.ijse.dep7.pos.pos.dao;

import lk.ijse.dep7.pos.pos.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    void saveCustomer(Customer customer) throws Exception;
    void updateCustomer(Customer customer) throws Exception;
    void deleteCustomerById(String customerId) throws Exception;
    Optional<Customer> findCustomerById(String customerId) throws Exception;
    List<Customer> findAllCustomers() throws Exception;
    long countCustomers() throws Exception;
    boolean existsCustomerById(String customerId) throws Exception;
    List<Customer> findAllCustomers(int page, int size) throws Exception;
    String getLastCustomerId() throws Exception;
}
