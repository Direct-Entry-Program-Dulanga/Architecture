package lk.ijse.dep7.pos.pos.service;

import lk.ijse.dep7.pos.pos.dao.impl.ItemDAOImpl;
import lk.ijse.dep7.pos.pos.dto.ItemDTO;
import static lk.ijse.dep7.pos.pos.service.util.EntityDTOMapper.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ItemService {

    private Connection connection;
    private ItemDAOImpl itemDAOImpl;

    public ItemService() {
    }

    public ItemService(Connection connection) {
        this.itemDAOImpl = new ItemDAOImpl(connection);
    }

    public void saveItem(ItemDTO item) throws SQLException {
        if (existItem(item.getCode())) {
            throw new RuntimeException(item.getCode() + " already exists");
        }
        itemDAOImpl.saveItem(fromItemDTO(item));
    }

    public void updateItem(ItemDTO item) throws SQLException {
        if (!existItem(item.getCode())) {
            throw new RuntimeException("There is no such item associated with the code " + item.getCode());
        }
        itemDAOImpl.updateItem(fromItemDTO(item));
    }

    public void deleteItem(String code) throws SQLException {
        if (!existItem(code)) {
            throw new RuntimeException("There is no such item associated with the code " + code);
        }
        itemDAOImpl.deleteItemByCode(code);
    }

    public ItemDTO findItem(String code) throws SQLException {
        return toItemDTO(itemDAOImpl.findItemByCode(code).<RuntimeException>orElseThrow(() -> {throw new RuntimeException("There is no such customer associated with the id " + code);}));
    }

    public List<ItemDTO> findAllItems() throws SQLException {
        return toItemDTOList(itemDAOImpl.findAllItems());
    }

    public List<ItemDTO> findAllItems(int page, int size) throws SQLException {
        return toItemDTOList(itemDAOImpl.findAllItems(page, size));
    }

    public long getItemsCount() throws Exception {
        return itemDAOImpl.countItems();
    }

    boolean existItem(String code) throws SQLException {
        return itemDAOImpl.existsItemByCode(code);
    }

    public String generateNewItemCode() throws SQLException {

        String code = itemDAOImpl.getLastItemCode();
        if (code != null) {

            int newItemCode = Integer.parseInt(code.replace("I", "")) + 1;
            return String.format("I%03d", newItemCode);
        } else {
            return "I001";
        }
    }

}
