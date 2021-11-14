package lk.ijse.dep7.pos.pos.service;

import lk.ijse.dep7.pos.pos.dao.ItemDAO;
import lk.ijse.dep7.pos.pos.dto.ItemDTO;
import lk.ijse.dep7.pos.pos.exception.DuplicateIdentifierException;
import lk.ijse.dep7.pos.pos.exception.FailedOperationException;
import lk.ijse.dep7.pos.pos.exception.NotFoundException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ItemService {

    private Connection connection;
    private ItemDAO itemDAO;

    public ItemService() {
    }

    public ItemService(Connection connection) {
        this.itemDAO = new ItemDAO(connection);
    }

    public void saveItem(ItemDTO item) throws DuplicateIdentifierException, SQLException {
        if (existItem(item.getCode())) {
            throw new DuplicateIdentifierException(item.getCode() + " already exists");
        }
        ItemDAO.saveItem(item);
    }

    private boolean existItem(String code) {
        return ItemDAO.existItem(code);
    }

    public void updateItem(ItemDTO item) throws NotFoundException {
        if (!existItem(item.getCode())) {
            throw new NotFoundException("There is no such item associated with the code " + item.getCode());
        }
        itemDAO.updateItem(item);
    }

    public void deleteItem(String code) throws NotFoundException {
        if (!existItem(code)) {
            throw new NotFoundException("There is no such item associated with the code " + code);
        }
        itemDAO.deleteItem(code);
    }

    public ItemDTO findItem(String code) throws NotFoundException {
        if (!existItem(code)) {
            throw new NotFoundException("There is no such item associated with the id " + code);
        }
        return itemDAO.findItem(code);
    }

    public List<ItemDTO> findAllItems() throws FailedOperationException {
        return ItemDAO.findAllItems();
    }

    public List<ItemDTO> findAllItems(int page, int size) throws FailedOperationException {
        return ItemDAO.findAllItems(page, size);
    }

    public String generateNewItemCode() throws FailedOperationException {

        String code = itemDAO.getLastItemCode();
        if (code != null) {

            int newItemCode = Integer.parseInt(code.replace("I", "")) + 1;
            return String.format("I%03d", newItemCode);
        } else {
            return "I001";
        }
    }

}
