package lk.ijse.dep7.pos.pos.service;

import lk.ijse.dep7.pos.pos.dao.ItemDAO;
import lk.ijse.dep7.pos.pos.dao.impl.ItemDAOImpl;
import lk.ijse.dep7.pos.pos.dto.ItemDTO;
import static lk.ijse.dep7.pos.pos.service.util.EntityDTOMapper.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ItemService {

    private Connection connection;
    private ItemDAO itemDAO;

    public ItemService() {
    }

    public ItemService(Connection connection) {
        this.itemDAO = new ItemDAOImpl(connection);
    }

    public void saveItem(ItemDTO item) throws Exception {
        if (existItem(item.getCode())) {
            throw new RuntimeException(item.getCode() + " already exists");
        }
        itemDAO.saveItem(fromItemDTO(item));
    }

    public void updateItem(ItemDTO item) throws Exception {
        if (!existItem(item.getCode())) {
            throw new RuntimeException("There is no such item associated with the code " + item.getCode());
        }
        itemDAO.updateItem(fromItemDTO(item));
    }

    public void deleteItem(String code) throws Exception {
        if (!existItem(code)) {
            throw new RuntimeException("There is no such item associated with the code " + code);
        }
        itemDAO.deleteItemByCode(code);
    }

    public ItemDTO findItem(String code) throws Exception {
        return toItemDTO(itemDAO.findItemByCode(code).<RuntimeException>orElseThrow(() -> {throw new RuntimeException("There is no such customer associated with the id " + code);}));
    }

    public List<ItemDTO> findAllItems() throws Exception {
        return toItemDTOList(itemDAO.findAllItems());
    }

    public List<ItemDTO> findAllItems(int page, int size) throws Exception {
        return toItemDTOList(itemDAO.findAllItems(page, size));
    }

    public long getItemsCount() throws Exception {
        return itemDAO.countItems();
    }

    boolean existItem(String code) throws Exception {
        return itemDAO.existsItemByCode(code);
    }

    public String generateNewItemCode() throws Exception {

        String code = itemDAO.getLastItemCode();
        if (code != null) {

            int newItemCode = Integer.parseInt(code.replace("I", "")) + 1;
            return String.format("I%03d", newItemCode);
        } else {
            return "I001";
        }
    }

}
