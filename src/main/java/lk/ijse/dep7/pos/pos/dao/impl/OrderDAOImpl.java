package lk.ijse.dep7.pos.pos.dao.impl;

import lk.ijse.dep7.pos.pos.dto.OrderDTO;
import lk.ijse.dep7.pos.pos.entity.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAOImpl {

    private static Connection connection;

    public OrderDAOImpl(Connection connection) {
        this.connection = connection;
    }


    public void saveOrder(Order order) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("INSERT INTO `order` values (?,?,?)");
        stm.setString(1, order.getId());
        stm.setDate(2, order.getDate());
        stm.setString(3, order.getCustomerId());
        stm.executeUpdate();
    }

    public void updateOrder(Order order) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("UPDATE `order` SET date=?, customer_id=? WHERE id=?");
        stm.setDate(2, order.getDate());
        stm.setString(3, order.getCustomerId());
        stm.setString(1, order.getId());
        stm.executeUpdate();
    }

    public void deleteOrderById(String orderId) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("DELETE FROM `order` WHERE id=?");
        stm.setString(1, orderId);
        stm.executeUpdate();
    }

    public Optional<Order> findOrderById(String orderId) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM `order` WHERE id=?");
        pstm.setString(1, orderId);
        ResultSet rst = pstm.executeQuery();
        return  (rst.next()) ? Optional.of(new Order(orderId, rst.getDate("date"), rst.getString("customer_id"))) : Optional.empty();
    }

    public List<Order> findAllOrders() throws SQLException {
        List<Order> ordersList = new ArrayList<>();

        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT * FROM `order`");

        while (rst.next()) {
            ordersList.add(new Order(rst.getString("id"), rst.getDate("date"), rst.getString("customer_id")));
        }

        return ordersList;
    }

    public long countOrders() throws SQLException {
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT COUNT(*) FROM `order`");
        rst.next();
        return rst.getLong(1);
    }

    public static String getLastOrderId() throws SQLException {
            ResultSet rst = connection.createStatement().executeQuery("SELECT id FROM `order` ORDER BY id DESC LIMIT 1;");
            return rst.next()? rst.getString("id"): null;
    }

    public boolean existsOrderById(String orderId) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT id FROM `order` WHERE id=?");
        stm.setString(1, orderId);
        return stm.executeQuery().next();
    }


//    public static void saveOrder(String orderId, LocalDate orderDate, String customerId) {
//        try {
//            PreparedStatement stm = connection.prepareStatement("INSERT INTO `order` (id, date, customer_id) VALUES (?,?,?)");
//            stm.setString(1, orderId);
//            stm.setDate(2, Date.valueOf(orderDate));
//            stm.setString(3, customerId);
//            stm.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException("Failed to save order", e);
//        }
//    }
//
//    public static String getLastOrderId() {
//        try {
//            ResultSet rst = connection.createStatement().executeQuery("SELECT id FROM `order` ORDER BY id DESC LIMIT 1;");
//
//            if (rst.next()) {
//                String id = rst.getString("id");
//                return id;
//            } else {
//                return null;
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException("Failed to generate a new Order id", e);
//        }
//    }
//
//    public boolean existOrder(String orderId) {
//        try {
//            PreparedStatement stm = connection.prepareStatement("SELECT id FROM `order` WHERE id=?");
//            stm.setString(1, orderId);
//            return stm.executeQuery().next();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed Operation", e);
//        }
//    }
//
//    public void saveOrderDetail(String orderId, OrderDetailDTO orderDetail) {
//        try {
//            PreparedStatement stm = connection.prepareStatement("INSERT INTO order_detail (order_id, item_code, unit_price, qty) VALUES (?,?,?,?)");
//            stm.setString(1, orderId);
//            stm.setString(2, orderDetail.getItemCode());
//            stm.setBigDecimal(3, orderDetail.getUnitPrice());
//            stm.setInt(4, orderDetail.getQty());
//            stm.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException("Failed to save the order detail", e);
//        }
//    }
//
//    public List<OrderDTO> searchOrders(String query) {
//        List<OrderDTO> orderList = new ArrayList<>();
//
//        try {
//            String[] searchWords = query.split("\\s");
//            StringBuilder sqlBuilder = new StringBuilder("SELECT o.*, c.name, order_total.total\n" +
//                    "FROM `order` o\n" +
//                    "         INNER JOIN customer c on o.customer_id = c.id\n" +
//                    "         INNER JOIN\n" +
//                    "     (SELECT order_id, SUM(qty * unit_price) AS total FROM order_detail od GROUP BY order_id) AS order_total\n" +
//                    "     ON o.id = order_total.order_id\n" +
//                    "WHERE (order_id LIKE ?\n" +
//                    "    OR date LIKE ?\n" +
//                    "    OR customer_id LIKE ?\n" +
//                    "    OR name LIKE ?) ");
//
//            for (int i = 1; i < searchWords.length; i++) {
//                sqlBuilder.append("AND (\n" +
//                        "            order_id LIKE ?\n" +
//                        "        OR date LIKE ?\n" +
//                        "        OR customer_id LIKE ?\n" +
//                        "        OR name LIKE ?)");
//            }
//            PreparedStatement stm = connection.prepareStatement(sqlBuilder.toString());
//
//            for (int i = 0; i < searchWords.length * 4; i++) {
//                stm.setString(i + 1, "%" + searchWords[(i / 4)] + "%");
//            }
//            ResultSet rst = stm.executeQuery();
//
//            while (rst.next()) {
//                orderList.add(new OrderDTO(rst.getString("id"), rst.getDate("date").toLocalDate(),
//                        rst.getString("customer_id"), rst.getString("name"), rst.getBigDecimal("total")));
//            }
//
//            return orderList;
//        } catch (SQLException e) {
//            throw new RuntimeException("Failed to search orders", e);
//        }
//    }
//
//    public long getSearchOrdersCount(String query) {
//        try {
//            String[] searchWords = query.split("\\s");
//            StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(*) \n" +
//                    "FROM `order` o\n" +
//                    "         INNER JOIN customer c on o.customer_id = c.id\n" +
//                    "         INNER JOIN\n" +
//                    "     (SELECT order_id, SUM(qty * unit_price) AS total FROM order_detail od GROUP BY order_id) AS order_total\n" +
//                    "     ON o.id = order_total.order_id\n" +
//                    "WHERE (order_id LIKE ?\n" +
//                    "    OR date LIKE ?\n" +
//                    "    OR customer_id LIKE ?\n" +
//                    "    OR name LIKE ?) ");
//
//            for (int i = 1; i < searchWords.length; i++) {
//                sqlBuilder.append("AND (\n" +
//                        "            order_id LIKE ?\n" +
//                        "        OR date LIKE ?\n" +
//                        "        OR customer_id LIKE ?\n" +
//                        "        OR name LIKE ?)");
//            }
//            PreparedStatement stm = connection.prepareStatement(sqlBuilder.toString());
//
//            for (int i = 0; i < searchWords.length * 4; i++) {
//                stm.setString(i + 1, "%" + searchWords[(i / 4)] + "%");
//            }
//
//            ResultSet rst = stm.executeQuery();
//            rst.next();
//            return rst.getLong(1);
//        } catch (SQLException e) {
//            throw new RuntimeException("Failed to get search count", e);
//        }
//    }
//
//    public List<OrderDTO> searchOrders(String query, int page, int size) {
//        List<OrderDTO> orderList = new ArrayList<>();
//        try {
//            String[] searchWords = query.split("\\s");
//            StringBuilder sqlBuilder = new StringBuilder("SELECT o.*, c.name, order_total.total\n" +
//                    "FROM `order` o\n" +
//                    "         INNER JOIN customer c on o.customer_id = c.id\n" +
//                    "         INNER JOIN\n" +
//                    "     (SELECT order_id, SUM(qty * unit_price) AS total FROM order_detail od GROUP BY order_id) AS order_total\n" +
//                    "     ON o.id = order_total.order_id\n" +
//                    "WHERE (order_id LIKE ?\n" +
//                    "    OR date LIKE ?\n" +
//                    "    OR customer_id LIKE ?\n" +
//                    "    OR name LIKE ?) ");
//
//            for (int i = 1; i < searchWords.length; i++) {
//                sqlBuilder.append("AND (\n" +
//                        "            order_id LIKE ?\n" +
//                        "        OR date LIKE ?\n" +
//                        "        OR customer_id LIKE ?\n" +
//                        "        OR name LIKE ?)");
//            }
//            sqlBuilder.append(" LIMIT ? OFFSET ?");
//            PreparedStatement stm = connection.prepareStatement(sqlBuilder.toString());
//
//            for (int i = 0; i < searchWords.length * 4; i++) {
//                stm.setString(i + 1, "%" + searchWords[(i / 4)] + "%");
//            }
//            stm.setInt((searchWords.length * 4) + 1, size);
//            stm.setInt((searchWords.length * 4) + 2, size * (page - 1));
//            ResultSet rst = stm.executeQuery();
//
//            while (rst.next()) {
//                orderList.add(new OrderDTO(rst.getString("id"), rst.getDate("date").toLocalDate(),
//                        rst.getString("customer_id"), rst.getString("name"), rst.getBigDecimal("total")));
//            }
//
//            return orderList;
//        } catch (SQLException e) {
//            throw new RuntimeException("Failed to search orders with page", e);
//        }
//    }
//
//    public List<OrderDetailDTO> findOrderDetails(String orderId) {
//        List<OrderDetailDTO> orderDetailsList = new ArrayList<>();
//
//        try {
//            PreparedStatement stm = connection.prepareStatement("SELECT id FROM `order` WHERE id=?");
//            stm.setString(1, orderId);
//
//            if (!stm.executeQuery().next()) throw new RuntimeException("Invalid order id");
//
//            stm = connection.prepareStatement("SELECT * FROM order_detail WHERE order_id=?");
//            stm.setString(1, orderId);
//            ResultSet rst = stm.executeQuery();
//
//            while (rst.next()) {
//                orderDetailsList.add(new OrderDetailDTO(rst.getString("item_code"),
//                        rst.getInt("qty"),
//                        rst.getBigDecimal("unit_price")));
//            }
//
//            return orderDetailsList;
//        } catch (SQLException e) {
//            throw new RuntimeException("Failed to find order details" + orderId, e);
//        }
//    }
}
