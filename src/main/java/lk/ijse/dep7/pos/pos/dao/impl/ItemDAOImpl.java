package lk.ijse.dep7.pos.pos.dao.impl;

import lk.ijse.dep7.pos.pos.entity.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemDAOImpl {
    private static Connection connection;

    public ItemDAOImpl(Connection connection) {
        ItemDAOImpl.connection = connection;
    }

    public static void saveItem(Item item) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO item (code, description, unit_price, qty_on_hand) VALUES (?,?,?,?)");
        pstm.setString(1, item.getCode());
        pstm.setString(2, item.getDescription());
        pstm.setBigDecimal(3, item.getUnitPrice());
        pstm.setInt(4, item.getQtyOnHand());
        pstm.executeUpdate();
    }

    public void updateItem(Item item) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement("UPDATE item SET description=?, unit_price=?, qty_on_hand=? WHERE code=?");
        pstm.setString(1, item.getCode());
        pstm.setString(2, item.getDescription());
        pstm.setBigDecimal(3, item.getUnitPrice());
        pstm.setInt(4, item.getQtyOnHand());
        pstm.executeUpdate();
    }

    public void deleteItemByCode(String itemCode) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement("DELETE FROM item WHERE code=?");
        pstm.setString(1, itemCode);
        pstm.executeUpdate();
    }

    public Optional<Item> findItemByCode(String itemCode) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM item WHERE code=?");
        pstm.setString(1, itemCode);
        ResultSet rst = pstm.executeQuery();
        return  (rst.next()) ? Optional.of(new Item(itemCode, rst.getString("description"), rst.getBigDecimal("unit_price"), rst.getInt("qty_on_hand"))) :
                Optional.empty();
    }

    public List<Item> findAllItems() throws SQLException {
        List<Item> itemList = new ArrayList<>();
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT * FROM item");

        while (rst.next()) {
            itemList.add(new Item(rst.getString("code"), rst.getString("description"), rst.getBigDecimal("unit_price"), rst.getInt("qty_on_hand")));
        }
        return itemList;
    }

    public static List<Item> findAllItems(int page, int size) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM item LIMIT ? OFFSET ?;");
        stm.setObject(1, size);
        stm.setObject(2, size * (page - 1));
        ResultSet rst = stm.executeQuery();
        List<Item> itemList = new ArrayList<>();

        while (rst.next()) {
            itemList.add(new Item(rst.getString("code"), rst.getString("description"), rst.getBigDecimal("unit_price"), rst.getInt("qty_on_hand")));
        }
        return itemList;
    }

    public static String getLastItemCode() throws SQLException {
        ResultSet rst = connection.createStatement().executeQuery("SELECT code FROM item ORDER BY code DESC LIMIT 1;");
        return rst.next()? rst.getString("code"): null;
    }

    public long countItems() throws Exception {
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT COUNT(*) FROM item");
        rst.next();
        return rst.getLong(1);
    }

    public static boolean existItem(String itemCode) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement("SELECT code FROM item WHERE code=?");
        pstm.setString(1, itemCode);
        return pstm.executeQuery().next();
    }

}
