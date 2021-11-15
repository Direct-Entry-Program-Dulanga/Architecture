package lk.ijse.dep7.pos.pos.dao;

import lk.ijse.dep7.pos.pos.entity.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    private static Connection connection;

    public CustomerDAO(Connection connection) {
        this.connection = connection;
    }

    public void saveCustomer(Customer customer) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("INSERT INTO customer values (?,?,?)");
        stm.setString(1, customer.getId());
        stm.setString(2, customer.getName());
        stm.setString(3, customer.getAddress());
        stm.executeUpdate();
    }

    public void updateCustomer(Customer customer) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement("UPDATE customer SET name=?, address=? WHERE id=?");
        pstm.setString(1, customer.getId());
        pstm.setString(2, customer.getName());
        pstm.setString(3, customer.getAddress());
        pstm.executeUpdate();
    }

    public void deleteCustomerById(String customerId) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement("DELETE FROM customer WHERE id=?");
        pstm.setString(1, customerId);
        pstm.executeUpdate();
    }

    public Customer findCustomerById(String customerId) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM customer WHERE id=?");
        pstm.setString(1, customerId);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return new Customer(customerId, rst.getString("name"), rst.getString("address"));
        } else {
            throw new RuntimeException(customerId + "is not found");
        }
    }

    public List<Customer> findAllCustomers() throws SQLException {
        List<Customer> customersList = new ArrayList<>();

        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT * FROM customer");

        while (rst.next()) {
            customersList.add(new Customer(rst.getString("id"), rst.getString("name"), rst.getString("address")));
        }

        return customersList;
    }

    public static List<Customer> findAllCustomers(int page, int size) throws SQLException {
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

    public static String getLastCustomerId() throws SQLException {
        ResultSet rst = connection.createStatement().executeQuery("SELECT id FROM customer ORDER BY id DESC LIMIT 1;");
        return rst.next()? rst.getString("id"): null;
    }

    public boolean existsCustomerById(String customerId) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement("SELECT id FROM customer WHERE id=?");
        pstm.setString(1, customerId);
        return pstm.executeQuery().next();
    }
}
