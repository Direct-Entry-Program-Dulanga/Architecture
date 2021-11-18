package lk.ijse.dep7.pos.pos.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CustomEntity implements Serializable {
    private String orderId;
    private Date orderDate;
    private String customerId;
    private String CustomerName;
    private BigDecimal orderTotal;

    public CustomEntity() {
    }

    public CustomEntity(String orderId, Date orderDate, String customerId, String customerName) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customerId = customerId;
        CustomerName = customerName;
    }

    public CustomEntity(String orderId, Date orderDate, String customerId, String customerName, BigDecimal orderTotal) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customerId = customerId;
        CustomerName = customerName;
        this.orderTotal = orderTotal;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
    }

    @Override
    public String toString() {
        return "CustomEntity{" +
                "orderId='" + orderId + '\'' +
                ", orderDate=" + orderDate +
                ", customerId='" + customerId + '\'' +
                ", CustomerName='" + CustomerName + '\'' +
                ", orderTotal=" + orderTotal +
                '}';
    }
}
