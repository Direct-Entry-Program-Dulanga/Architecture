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
    public void save(Object entity) throws SQLException {
        Item item = (Item) entity;
        PreparedStatement stm = connection.prepareStatement("INSERT INTO item (code, description, unit_price, qty_on_hand) VALUES (?,?,?,?)");
        stm.setString(1, item.getCode());
        stm.setString(2, item.getDescription());
        stm.setBigDecimal(3, item.getUnitPrice());
        stm.setInt(4, item.getQtyOnHand());
        stm.executeUpdate();
    }

    @Override
    public void update(Object entity) throws SQLException {
        Item item = (Item) entity;
        PreparedStatement stm = connection.prepareStatement("UPDATE item SET description=?, unit_price=?, qty_on_hand=? WHERE code=?");
        stm.setString(1, item.getCode());
        stm.setString(2, item.getDescription());
        stm.setBigDecimal(3, item.getUnitPrice());
        stm.setInt(4, item.getQtyOnHand());
        stm.executeUpdate();
    }

    @Override
    public void deleteById(Object key) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement("DELETE FROM item WHERE code=?");
        pstm.setString(1, key.toString());
        pstm.executeUpdate();
    }

    @Override
    public Optional<Object> findById(Object key) throws Exception {
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM item WHERE code=?");
        pstm.setString(1, key.toString());
        ResultSet rst = pstm.executeQuery();
        return  (rst.next()) ? Optional.of(new Item(key.toString(), rst.getString("description"), rst.getBigDecimal("unit_price"), rst.getInt("qty_on_hand"))) :
                Optional.empty();
    }

    @Override
    public List<Object> findAll() throws SQLException {
        List<Object> itemList = new ArrayList<>();
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT * FROM item");

        while (rst.next()) {
            itemList.add(new Item(rst.getString("code"), rst.getString("description"), rst.getBigDecimal("unit_price"), rst.getInt("qty_on_hand")));
        }
        return itemList;
    }

    @Override
    public List<Object> findAll(int page, int size) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM item LIMIT ? OFFSET ?;");
        stm.setObject(1, size);
        stm.setObject(2, size * (page - 1));
        ResultSet rst = stm.executeQuery();
        List<Object> itemList = new ArrayList<>();

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
    public long count() throws Exception {
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT COUNT(*) FROM item");
        rst.next();
        return rst.getLong(1);
    }

    @Override
    public boolean existsById(Object key) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT code FROM item WHERE code=?");
        stm.setString(1, key.toString());
        return stm.executeQuery().next();
    }

}
