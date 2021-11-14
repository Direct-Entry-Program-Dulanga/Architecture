package lk.ijse.dep7.pos.pos.dao;

import lk.ijse.dep7.pos.pos.dto.OrderDetailDTO;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class OrderDAO {

    private static Connection connection;

    public OrderDAO(Connection connection){
        this.connection = connection;
    }

    public static void saveOrder(String orderId, LocalDate orderDate, String customerId, List<OrderDetailDTO> orderDetails){

    }
}
