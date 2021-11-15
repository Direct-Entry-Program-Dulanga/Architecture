package lk.ijse.dep7.pos.pos.dao;

import lk.ijse.dep7.pos.pos.entity.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QueryDAO {

    private Connection connection;

    public QueryDAO(Connection connection){
        this.connection = connection;
    }

    public List<HashMap<String, Object>> searchOrders(String query) throws SQLException {
        List<HashMap<String, Object>> orderList = new ArrayList<>();

        String[] searchWords = query.split("\\s");
        StringBuilder sqlBuilder = new StringBuilder("SELECT o.*, c.name, order_total.total\n" +
                "FROM `order` o\n" +
                "         INNER JOIN customer c on o.customer_id = c.id\n" +
                "         INNER JOIN\n" +
                "     (SELECT order_id, SUM(qty * unit_price) AS total FROM order_detail od GROUP BY order_id) AS order_total\n" +
                "     ON o.id = order_total.order_id\n" +
                "WHERE (order_id LIKE ?\n" +
                "    OR date LIKE ?\n" +
                "    OR customer_id LIKE ?\n" +
                "    OR name LIKE ?) ");

        for (int i = 1; i < searchWords.length; i++) {
            sqlBuilder.append("AND (\n" +
                    "            order_id LIKE ?\n" +
                    "        OR date LIKE ?\n" +
                    "        OR customer_id LIKE ?\n" +
                    "        OR name LIKE ?)");
        }
        PreparedStatement stm = connection.prepareStatement(sqlBuilder.toString());

        for (int i = 0; i < searchWords.length * 4; i++) {
            stm.setString(i + 1, "%" + searchWords[(i / 4)] + "%");
        }
        ResultSet rst = stm.executeQuery();

        while (rst.next()) {
            HashMap<String, Object> newRecord = new HashMap<>();
            newRecord.put("id", rst.getString("id"));
            newRecord.put("date", rst.getDate("date").toLocalDate());
            newRecord.put("customer_id", rst.getString("customer_id"));
            newRecord.put("name", rst.getString("name"));
            newRecord.put("total", rst.getBigDecimal("total"));
            orderList.add(newRecord);
        }

        return orderList;
    }
}
