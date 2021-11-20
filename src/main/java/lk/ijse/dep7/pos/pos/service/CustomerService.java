package lk.ijse.dep7.pos.pos.service;

import lk.ijse.dep7.pos.pos.dao.impl.CustomerDAOImpl;
import lk.ijse.dep7.pos.pos.dto.CustomerDTO;

import static lk.ijse.dep7.pos.pos.service.util.EntityDTOMapper.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CustomerService {

    private Connection connection;
    private CustomerDAOImpl customerDAOImpl;

    public CustomerService() {

    }

    public CustomerService(Connection connection) {
        this.customerDAOImpl = new CustomerDAOImpl(connection);
    }

    public void saveCustomer(CustomerDTO customer) throws SQLException {
        if (existCustomer(customer.getId())) {
            throw new RuntimeException(customer.getId() + " already exists");
        }
        customerDAOImpl.saveCustomer(fromCustomerDTO(customer));
    }

    public long getCustomersCount() throws Exception {
        return customerDAOImpl.countCustomers();
    }

    boolean existCustomer(String id) throws SQLException {
        return customerDAOImpl.existsCustomerById(id);
    }

    public void updateCustomer(CustomerDTO customer) throws SQLException {
        if (!existCustomer(customer.getId())) {
            throw new RuntimeException("There is no such customer associated with the id " + customer.getId());
        }
        customerDAOImpl.updateCustomer(fromCustomerDTO(customer));
    }

    public void deleteCustomer(String id) throws SQLException {
        if (!existCustomer(id)) {
            throw new RuntimeException("There is no such customer associated with the id " + id);
        }
        customerDAOImpl.deleteCustomerById(id);
    }

    public CustomerDTO findCustomer(String id) throws SQLException {
//        Optional<Customer> optCustomer = customerDAO.findCustomerById(id);
//        if(optCustomer.isPresent()){
//            throw new RuntimeException("There is no such customer associated with the id " + id);
//        }else {
//            Customer customer = optCustomer.get();
//            return new CustomerDTO(customer.getId(), customer.getName(), customer.getAddress());
//        }
        return toCustomerDTO(customerDAOImpl.findCustomerById(id).<RuntimeException>orElseThrow(() -> {throw new RuntimeException("There is no such customer associated with the id " + id);}));
    }

    public List<CustomerDTO> findAllCustomers() throws SQLException {
//        List<Customer> allCustomers = customerDAO.findAllCustomers();
//        List<CustomerDTO> dtoList = new ArrayList<>();
//        allCustomers.forEach(c -> dtoList.add(new CustomerDTO(c.getId(), c.getName(), c.getAddress())));
//        return dtoList;

//        return customerDAO.findAllCustomers().stream().map(c -> new CustomerDTO(c.getId(), c.getName(), c.getAddress())).collect(Collectors.toList());
        return toCustomerDTOList(customerDAOImpl.findAllCustomers());
    }

    public List<CustomerDTO> findAllCustomers(int page, int size) throws SQLException {
//        return customerDAO.(page, size);
//        return customerDAO.findAllCustomers(page, size).stream().map(c -> new CustomerDTO(c.getId(), c.getName(), c.getAddress())).collect(Collectors.toList());
        return toCustomerDTOList(customerDAOImpl.findAllCustomers(page, size));
    }

    public String generateNewCustomerId() throws SQLException {

        String id = customerDAOImpl.getLastCustomerId();
        if (id != null) {

            int newCustomerId = Integer.parseInt(id.replace("C", "")) + 1;
            return String.format("C%03d", newCustomerId);
        } else {
            return "C001";
        }
    }

}
