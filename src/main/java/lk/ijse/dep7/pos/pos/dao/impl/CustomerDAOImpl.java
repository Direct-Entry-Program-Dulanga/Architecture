package lk.ijse.dep7.pos.pos.dao.impl;

import lk.ijse.dep7.pos.pos.dao.CustomerDAO;
import lk.ijse.dep7.pos.pos.entity.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDAOImpl implements CustomerDAO {

    private static Connection connection;

    public CustomerDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveCustomer(Customer customer) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("INSERT INTO customer values (?,?,?)");
        stm.setString(1, customer.getId());
        stm.setString(2, customer.getName());
        stm.setString(3, customer.getAddress());
        stm.executeUpdate();
    }

    @Override
    public void updateCustomer(Customer customer) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("UPDATE customer SET name=?, address=? WHERE id=?");
        stm.setString(1, customer.getId());
        stm.setString(2, customer.getName());
        stm.setString(3, customer.getAddress());
        stm.executeUpdate();
    }

    @Override
    public void deleteCustomerById(String customerId) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("DELETE FROM customer WHERE id=?");
        stm.setString(1, customerId);
        stm.executeUpdate();
    }

    @Override
    public Optional<Customer> findCustomerById(String customerId) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM customer WHERE id=?");
        stm.setString(1, customerId);
        ResultSet rst = stm.executeQuery();
        return (rst.next())? Optional.of(new Customer(customerId, rst.getString("name"), rst.getString("address")))
            : Optional.empty();
    }

    @Override
    public List<Customer> findAllCustomers() throws SQLException {
        List<Customer> customersList = new ArrayList<>();

        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT * FROM customer");

        while (rst.next()) {
            customersList.add(new Customer(rst.getString("id"), rst.getString("name"), rst.getString("address")));
        }

        return customersList;
    }

    @Override
    public List<Customer> findAllCustomers(int page, int size) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM customer LIMIT ? OFFSET ?;");
        stm.setObject(1, size);
        stm.setObject(2, size * (page - 1));
        ResultSet rst = stm.executeQuery();
        List<Customer> customersList = new ArrayList<>();

        while (rst.next()) {
            customersList.add(new Customer(rst.getString("id"), rst.getString("name"), rst.getString("address")));
        }
        return customersList;
    }

    @Override
    public String getLastCustomerId() throws SQLException {
        ResultSet rst = connection.createStatement().executeQuery("SELECT id FROM customer ORDER BY id DESC LIMIT 1;");
        return rst.next()? rst.getString("id"): null;
    }

    @Override
    public long countCustomers() throws Exception {
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT COUNT(*) FROM customer");
        rst.next();
        return rst.getLong(1);
    }

    @Override
    public boolean existsCustomerById(String customerId) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement("SELECT id FROM customer WHERE id=?");
        pstm.setString(1, customerId);
        return pstm.executeQuery().next();
    }
}
