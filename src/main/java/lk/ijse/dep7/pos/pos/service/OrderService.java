package lk.ijse.dep7.pos.pos.service;

import lk.ijse.dep7.pos.pos.dao.impl.CustomerDAOImpl;
import lk.ijse.dep7.pos.pos.dao.impl.OrderDAOImpl;
import lk.ijse.dep7.pos.pos.dao.impl.OrderDetailDAOImpl;
import lk.ijse.dep7.pos.pos.dao.impl.QueryDAOImpl;
import lk.ijse.dep7.pos.pos.dto.ItemDTO;
import lk.ijse.dep7.pos.pos.dto.OrderDTO;
import lk.ijse.dep7.pos.pos.dto.OrderDetailDTO;
import lk.ijse.dep7.pos.pos.entity.Customer;
import lk.ijse.dep7.pos.pos.entity.Order;
import lk.ijse.dep7.pos.pos.entity.OrderDetail;
import lk.ijse.dep7.pos.pos.exception.FailedOperationException;

import static lk.ijse.dep7.pos.pos.service.util.EntityDTOMapper.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderService {

    private Connection connection;
    private CustomerDAOImpl customerDAOImpl;
    private OrderDAOImpl orderDAOImpl;
    private OrderDetailDAOImpl orderDetailDAOImpl;
    private QueryDAOImpl queryDAOImpl;

    public OrderService(Connection connection) {
        this.connection = connection;
        this.orderDAOImpl = new OrderDAOImpl(connection);
        this.orderDetailDAOImpl = new OrderDetailDAOImpl(connection);
        this.queryDAOImpl = new QueryDAOImpl(connection);
        this.customerDAOImpl = new CustomerDAOImpl(connection);
    }

    public void saveOrder(OrderDTO order) throws SQLException, FailedOperationException {

        final CustomerService customerService = new CustomerService(connection);
        final ItemService itemService = new ItemService(connection);
        final String orderId = order.getOrderId();
        final String customerId = order.getCustomerId();

        try {
            connection.setAutoCommit(false);

            if (orderDAOImpl.existsOrderById(orderId)) {
                throw new RuntimeException(order + " already exists");
            }

            if (!customerService.existCustomer(customerId)) {
                throw new RuntimeException("Customer id doesn't exist");
            }

            orderDAOImpl.saveOrder(fromOrderDTO(order));

//            stm = connection.prepareStatement("INSERT INTO order_detail (order_id, item_code, unit_price, qty) VALUES (?,?,?,?)");

            for (OrderDetailDTO detail : order.getOrderDetails()) {
                orderDetailDAOImpl.saveOrderDetail(fromOrderDetailDTO(orderId, detail));

                ItemDTO item = itemService.findItem(detail.getItemCode());
                item.setQtyOnHand(item.getQtyOnHand() - detail.getQty());
                itemService.updateItem(item);
            }

            connection.commit();
        } catch (SQLException e) {
            failedOperationExecutionContext(connection::rollback);
            throw e;
        } catch (Throwable t) {
            failedOperationExecutionContext(connection::rollback);
            throw t;
        } finally {
            failedOperationExecutionContext(() -> connection.setAutoCommit(true));
        }
    }

    public List<OrderDTO> searchOrders(String query) throws SQLException {
        return toOrderDTO1(queryDAOImpl.findOrders(query));
    }

    public long getSearchOrdersCount(String query) throws SQLException {
        return queryDAOImpl.countOrders(query);
    }

    public List<OrderDTO> searchOrders(String query, int page, int size) throws SQLException {
        return toOrderDTO2(queryDAOImpl.findOrders(query, page, size));
    }

    public OrderDTO searchOrder(String orderId) throws Exception {
        Order order = orderDAOImpl.findOrderById(orderId).<RuntimeException>orElseThrow(() -> {
            throw new RuntimeException("Invalid Order ID" + orderId);
        });
        Customer customer = customerDAOImpl.findCustomerById(order.getCustomerId()).get();
        BigDecimal orderTotal = orderDetailDAOImpl.findOrderTotal(orderId).get();
        List<OrderDetail> orderDetails = orderDetailDAOImpl.findOrderDetailsByOrderId(orderId);
        return toOrderDTO(order, customer, orderTotal,orderDetails);
    }

    public List<OrderDetailDTO> findOrderDetails(String orderId) {
        return null;
    }

    public String generateNewOrderId() throws SQLException {
        String id = orderDAOImpl.getLastOrderId();
        if (id != null) {
            return String.format("OD%03d", (Integer.parseInt(id.replace("OD", "")) + 1));
        } else {
            return "OD001";
        }
    }

    private void failedOperationExecutionContext(ExecutionContext context) throws FailedOperationException {
        try {
            context.execute();
        } catch (SQLException e) {
            throw new FailedOperationException("Failed to save the order", e);
        }
    }

    @FunctionalInterface
    interface ExecutionContext {
        void execute() throws SQLException;
    }

}
