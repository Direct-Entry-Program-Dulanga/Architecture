package lk.ijse.dep7.pos.pos.dao;

import lk.ijse.dep7.pos.pos.entity.OrderDetail;
import lk.ijse.dep7.pos.pos.entity.OrderDetailPK;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface OrderDetailDAO extends CrudDAO{

//    void saveOrderDetail(OrderDetail orderDetail) throws Exception;
//
//    void updateOrderDetail(OrderDetail orderDetail) throws Exception;
//
//    void deleteOrderDetailByPK(OrderDetailPK orderDetailPK) throws Exception;
//
//    Optional<OrderDetail> findOrderDetailById(OrderDetailPK orderDetailPK) throws Exception;
//
//    List<OrderDetail> findAllOrderDetails() throws Exception;
//
//    long countOrderDetails() throws Exception;
//
//    boolean existsOrderDetailById(OrderDetailPK orderDetailPK) throws Exception;
//
    Optional<BigDecimal> findOrderTotal(String orderId) throws Exception;

    List<OrderDetail> findOrderDetailsByOrderId(String orderId) throws Exception;


}
