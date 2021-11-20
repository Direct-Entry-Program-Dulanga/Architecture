package lk.ijse.dep7.pos.pos.dao;

import lk.ijse.dep7.pos.pos.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CrudDAO {
    void save(Object entity) throws Exception;
    void update(Object entity) throws Exception;
    void deleteById(Object key) throws Exception;
    Optional<Object> findCustomerById(Object key) throws Exception;
    List<Object> findAll() throws Exception;
    long count() throws Exception;
    boolean existsById(Object key) throws Exception;
    List<Object> findAll(int page, int size) throws Exception;
}
