package lk.ijse.dep7.pos.pos.service;

import lk.ijse.dep7.pos.pos.dao.CustomerDAO;
import lk.ijse.dep7.pos.pos.dto.CustomerDTO;
import lk.ijse.dep7.pos.pos.exception.DuplicateIdentifierException;
import lk.ijse.dep7.pos.pos.exception.FailedOperationException;
import lk.ijse.dep7.pos.pos.exception.NotFoundException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CustomerService {

    private Connection connection;
    private CustomerDAO customerDAO;

    public CustomerService() {

    }

    public CustomerService(Connection connection) {
        this.customerDAO = new CustomerDAO(connection);
    }

    public void saveCustomer(CustomerDTO customer) throws DuplicateIdentifierException, FailedOperationException {
        if (existCustomer(customer.getId())) {
            throw new DuplicateIdentifierException(customer.getId() + " already exists");
        }
        customerDAO.saveCustomer(customer);
    }

    public long getCustomersCount() throws SQLException {
        return customerDAO.getCustomersCount();
    }

    boolean existCustomer(String id) {
        return customerDAO.existCustomer(id);
    }

    public void updateCustomer(CustomerDTO customer) throws FailedOperationException, NotFoundException, SQLException {
        if (!existCustomer(customer.getId())) {
            throw new NotFoundException("There is no such customer associated with the id " + customer.getId());
        }
        customerDAO.updateCustomer(customer);
    }

    public void deleteCustomer(String id) throws NotFoundException, FailedOperationException {
        if (!existCustomer(id)) {
            throw new NotFoundException("There is no such customer associated with the id " + id);
        }
        customerDAO.deleteCustomer(id);
    }

    public CustomerDTO findCustomer(String id) throws NotFoundException, FailedOperationException {
        if (!existCustomer(id)) {
            throw new NotFoundException("There is no such customer associated with the id " + id);
        }
        return customerDAO.findCustomer(id);

    }

    public List<CustomerDTO> findAllCustomers() throws FailedOperationException {
        return customerDAO.findAllCustomer();
    }

    public List<CustomerDTO> findAllCustomers(int page, int size) throws FailedOperationException {
        return customerDAO.findAllCustomers(page, size);
    }

    public String generateNewCustomerId() throws FailedOperationException {

        String id = customerDAO.getLastCustomerId();
        if (id != null) {

            int newCustomerId = Integer.parseInt(id.replace("C", "")) + 1;
            return String.format("C%03d", newCustomerId);
        } else {
            return "C001";
        }
    }

}
