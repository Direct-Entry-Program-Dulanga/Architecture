package lk.ijse.dep7.pos.pos.dao;

import lk.ijse.dep7.pos.pos.entity.Customer;
import lk.ijse.dep7.pos.pos.entity.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDAO {
    void saveItem(Item item) throws Exception;
    void updateItem(Item item) throws Exception;
    void deleteItemByCode(String itemCode) throws Exception;
    Optional<Item> findItemByCode(String itemCode) throws Exception;
    List<Item> findAllItems() throws Exception;
    long countItems() throws Exception;
    boolean existsItemByCode(String itemCode) throws Exception;
    List<Item> findAllItems(int page, int size) throws Exception;
    String getLastItemCode() throws Exception;
}
