package lk.ijse.dep7.pos.pos.dao.impl;

import lk.ijse.dep7.pos.pos.dao.ItemDAO;
import lk.ijse.dep7.pos.pos.entity.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemDAOImpl implements ItemDAO {
    private static Connection connection;

    public ItemDAOImpl(Connection connection) {
        ItemDAOImpl.connection = connection;
    }
    @Override
    public void saveItem(Item item) throws SQLException {
        PreparedStatement pst = connection.prepareStatement("INSERT INTO item (code, description, unit_price, qty_on_hand) VALUES (?,?,?,?)");
        pst.setString(1, item.getCode());
        pst.setString(2, item.getDescription());
        pst.setBigDecimal(3, item.getUnitPrice());
        pst.setInt(4, item.getQtyOnHand());
        pst.executeUpdate();
    }

    @Override
    public void updateItem(Item item) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement("UPDATE item SET description=?, unit_price=?, qty_on_hand=? WHERE code=?");
        pstm.setString(1, item.getCode());
        pstm.setString(2, item.getDescription());
        pstm.setBigDecimal(3, item.getUnitPrice());
        pstm.setInt(4, item.getQtyOnHand());
        pstm.executeUpdate();
    }

    @Override
    public void deleteItemByCode(String itemCode) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement("DELETE FROM item WHERE code=?");
        pstm.setString(1, itemCode);
        pstm.executeUpdate();
    }

    @Override
    public Optional<Item> findItemByCode(String itemCode) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM item WHERE code=?");
        pstm.setString(1, itemCode);
        ResultSet rst = pstm.executeQuery();
        return  (rst.next()) ? Optional.of(new Item(itemCode, rst.getString("description"), rst.getBigDecimal("unit_price"), rst.getInt("qty_on_hand"))) :
                Optional.empty();
    }

    @Override
    public List<Item> findAllItems() throws SQLException {
        List<Item> itemList = new ArrayList<>();
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT * FROM item");

        while (rst.next()) {
            itemList.add(new Item(rst.getString("code"), rst.getString("description"), rst.getBigDecimal("unit_price"), rst.getInt("qty_on_hand")));
        }
        return itemList;
    }

    @Override
    public List<Item> findAllItems(int page, int size) throws SQLException {
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

    @Override
    public String getLastItemCode() throws SQLException {
        ResultSet rst = connection.createStatement().executeQuery("SELECT code FROM item ORDER BY code DESC LIMIT 1;");
        return rst.next()? rst.getString("code"): null;
    }

    @Override
    public long countItems() throws Exception {
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT COUNT(*) FROM item");
        rst.next();
        return rst.getLong(1);
    }

    @Override
    public boolean existsItemByCode(String itemCode) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT code FROM item WHERE code=?");
        stm.setString(1, itemCode);
        return stm.executeQuery().next();
    }

}
