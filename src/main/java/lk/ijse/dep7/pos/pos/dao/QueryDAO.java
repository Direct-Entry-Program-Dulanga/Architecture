package lk.ijse.dep7.pos.pos.dao;

import lk.ijse.dep7.pos.pos.entity.CustomEntity;
import lk.ijse.dep7.pos.pos.entity.Customer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface QueryDAO {

    List<HashMap<String, Object>> findOrders(String query) throws Exception;

    long countOrders(String query) throws Exception;

    List<CustomEntity> findOrders(String query, int page, int size) throws Exception;
}
