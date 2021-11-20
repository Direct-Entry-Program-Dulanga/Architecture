package lk.ijse.dep7.pos.pos.service;

import lk.ijse.dep7.pos.pos.dao.CustomerDAO;
import lk.ijse.dep7.pos.pos.dao.OrderDAO;
import lk.ijse.dep7.pos.pos.dao.OrderDetailDAO;
import lk.ijse.dep7.pos.pos.dao.QueryDAO;
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
    private CustomerDAO customerDAO;
    private OrderDAO orderDAO;
    private OrderDetailDAO orderDetailDAO;
    private QueryDAO queryDAO;

    public OrderService(Connection connection) {
        this.connection = connection;
        this.orderDAO = new OrderDAOImpl(connection);
        this.orderDetailDAO = new OrderDetailDAOImpl(connection);
        this.queryDAO = new QueryDAOImpl(connection);
        this.customerDAO = new CustomerDAOImpl(connection);
    }

    public void saveOrder(OrderDTO order) throws Exception {

        final CustomerService customerService = new CustomerService(connection);
        final ItemService itemService = new ItemService(connection);
        final String orderId = order.getOrderId();
        final String customerId = order.getCustomerId();

        try {
            connection.setAutoCommit(false);

            if (orderDAO.existsOrderById(orderId)) {
                throw new RuntimeException(order + " already exists");
            }

            if (!customerService.existCustomer(customerId)) {
                throw new RuntimeException("Customer id doesn't exist");
            }

            orderDAO.saveOrder(fromOrderDTO(order));

//            stm = connection.prepareStatement("INSERT INTO order_detail (order_id, item_code, unit_price, qty) VALUES (?,?,?,?)");

            for (OrderDetailDTO detail : order.getOrderDetails()) {
                orderDetailDAO.saveOrderDetail(fromOrderDetailDTO(orderId, detail));

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

    public List<OrderDTO> searchOrders(String query) throws Exception {
        return toOrderDTO1(queryDAO.findOrders(query));
    }

    public long getSearchOrdersCount(String query) throws Exception {
        return queryDAO.countOrders(query);
    }

    public List<OrderDTO> searchOrders(String query, int page, int size) throws Exception {
        return toOrderDTO2(queryDAO.findOrders(query, page, size));
    }

    public OrderDTO searchOrder(String orderId) throws Exception {
        Order order = orderDAO.findOrderById(orderId).<RuntimeException>orElseThrow(() -> {
            throw new RuntimeException("Invalid Order ID" + orderId);
        });
        Customer customer = customerDAO.findCustomerById(order.getCustomerId()).get();
        BigDecimal orderTotal = orderDetailDAO.findOrderTotal(orderId).get();
        List<OrderDetail> orderDetails = orderDetailDAO.findOrderDetailsByOrderId(orderId);
        return toOrderDTO(order, customer, orderTotal,orderDetails);
    }

    public List<OrderDetailDTO> findOrderDetails(String orderId) {
        return null;
    }

    public String generateNewOrderId() throws Exception {
        String id = orderDAO.getLastOrderId();
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
