package lk.ijse.dep7.pos.pos.service;

import lk.ijse.dep7.pos.pos.dao.CustomerDAO;
import lk.ijse.dep7.pos.pos.dao.impl.CustomerDAOImpl;
import lk.ijse.dep7.pos.pos.dto.CustomerDTO;

import static lk.ijse.dep7.pos.pos.service.util.EntityDTOMapper.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CustomerService {

    private Connection connection;
    private CustomerDAO customerDAO;

    public CustomerService() {

    }

    public CustomerService(Connection connection) {
        this.customerDAO = new CustomerDAOImpl(connection);
    }

    public void saveCustomer(CustomerDTO customer) throws Exception {
        if (existCustomer(customer.getId())) {
            throw new RuntimeException(customer.getId() + " already exists");
        }
        customerDAO.saveCustomer(fromCustomerDTO(customer));
    }

    public void updateCustomer(CustomerDTO customer) throws Exception {
        if (!existCustomer(customer.getId())) {
            throw new RuntimeException("There is no such customer associated with the id " + customer.getId());
        }
        customerDAO.updateCustomer(fromCustomerDTO(customer));
    }

    public void deleteCustomer(String id) throws Exception {
        if (!existCustomer(id)) {
            throw new RuntimeException("There is no such customer associated with the id " + id);
        }
        customerDAO.deleteCustomerById(id);
    }

    public CustomerDTO findCustomer(String id) throws Exception {
//        Optional<Customer> optCustomer = customerDAO.findCustomerById(id);
//        if(optCustomer.isPresent()){
//            throw new RuntimeException("There is no such customer associated with the id " + id);
//        }else {
//            Customer customer = optCustomer.get();
//            return new CustomerDTO(customer.getId(), customer.getName(), customer.getAddress());
//        }
        return toCustomerDTO(customerDAO.findCustomerById(id).<RuntimeException>orElseThrow(() -> {throw new RuntimeException("There is no such customer associated with the id " + id);}));
    }

    public List<CustomerDTO> findAllCustomers() throws Exception {
//        List<Customer> allCustomers = customerDAO.findAllCustomers();
//        List<CustomerDTO> dtoList = new ArrayList<>();
//        allCustomers.forEach(c -> dtoList.add(new CustomerDTO(c.getId(), c.getName(), c.getAddress())));
//        return dtoList;

//        return customerDAO.findAllCustomers().stream().map(c -> new CustomerDTO(c.getId(), c.getName(), c.getAddress())).collect(Collectors.toList());
        return toCustomerDTOList(customerDAO.findAllCustomers());
    }

    public List<CustomerDTO> findAllCustomers(int page, int size) throws Exception {
//        return customerDAO.(page, size);
//        return customerDAO.findAllCustomers(page, size).stream().map(c -> new CustomerDTO(c.getId(), c.getName(), c.getAddress())).collect(Collectors.toList());
        return toCustomerDTOList(customerDAO.findAllCustomers(page, size));
    }

    public long getCustomersCount() throws Exception {
        return customerDAO.countCustomers();
    }

    boolean existCustomer(String id) throws Exception {
        return customerDAO.existsCustomerById(id);
    }

    public String generateNewCustomerId() throws Exception {

        String id = customerDAO.getLastCustomerId();
        if (id != null) {

            int newCustomerId = Integer.parseInt(id.replace("C", "")) + 1;
            return String.format("C%03d", newCustomerId);
        } else {
            return "C001";
        }
    }

}
