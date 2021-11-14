package lk.ijse.dep7.pos.pos.dao;

import lk.ijse.dep7.pos.pos.dto.CustomerDTO;
import lk.ijse.dep7.pos.pos.dto.ItemDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {
    private static Connection connection;

    public ItemDAO(Connection connection){
        this.connection = connection;
    }

    public static void saveItem(ItemDTO item){
        try{
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO item (code, description, unit_price, qty_on_hand) VALUES (?,?,?,?)");
            pstm.setString(1, item.getCode());
            pstm.setString(2, item.getDescription());
            pstm.setBigDecimal(3, item.getUnitPrice());
            pstm.setInt(4, item.getQtyOnHand());
            pstm.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Failed to save the Item");
        }
    }

    public static long getItemCount(){
        try{
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT COUNT(*) FROM item");
            rst.next();
            return rst.getLong(1);
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Failed to get Items count");
        }
    }

    public static boolean existItem(String itemCode){
        try{
            PreparedStatement pstm = connection.prepareStatement("SELECT code FROM item WHERE code=?");
            pstm.setString(1, itemCode);
            return pstm.executeQuery().next();
        }catch (SQLException e){
            throw new RuntimeException("Failed operation");
        }
    }

    public static void updateItem(ItemDTO item){
        try{
            PreparedStatement pstm = connection.prepareStatement("UPDATE item SET description=?, unit_price=?, qty_on_hand=? WHERE code=?");
            pstm.setString(1, item.getCode());
            pstm.setString(2, item.getDescription());
            pstm.setBigDecimal(3, item.getUnitPrice());
            pstm.setInt(4, item.getQtyOnHand());
            pstm.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Failed to update the item" + item.getCode(), e);
        }
    }

    public static void deleteItem(String itemCode){
        try{
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM item WHERE code=?");
            pstm.setString(1, itemCode);
            pstm.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Failed to delete item" + itemCode, e);
        }
    }

    public static ItemDTO findItem(String itemCode){
        try{
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM item WHERE code=?");
            pstm.setString(1, itemCode);
            ResultSet rst = pstm.executeQuery();
            rst.next();
            return new ItemDTO(itemCode, rst.getString("description"), rst.getBigDecimal("unit_price"), rst.getInt("qty_on_hand"));
        }catch (SQLException e){
            throw new RuntimeException("Failed to find the customer" + itemCode, e);
        }
    }

    public static List<ItemDTO> findAllItems(){
        try {
            List<ItemDTO> itemList = new ArrayList<>();

            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM item");

            while (rst.next()) {
                itemList.add(new ItemDTO(rst.getString("code"), rst.getString("description"), rst.getBigDecimal("unit_price"), rst.getInt("qty_on_hand")));
            }

            return itemList;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find items", e);
        }
    }

    public static List<ItemDTO> findAllItems(int page, int size){
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM item LIMIT ? OFFSET ?;");
            stm.setObject(1, size);
            stm.setObject(2, size * (page - 1));
            ResultSet rst = stm.executeQuery();
            List<ItemDTO> itemList = new ArrayList<>();

            while (rst.next()) {
                itemList.add(new ItemDTO(rst.getString("code"), rst.getString("description"), rst.getBigDecimal("unit_price"), rst.getInt("qty_on_hand")));
            }
            return itemList;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch items", e);
        }
    }

    public static String getLastItemCode(){
        try {
            ResultSet rst = connection.createStatement().executeQuery("SELECT code FROM item ORDER BY code DESC LIMIT 1;");

            if (rst.next()) {
                String code = rst.getString("code");
                return code;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to generate a new code", e);
        }
    }

}
