package lk.ijse.dep7.pos.pos.service.util;

import lk.ijse.dep7.pos.pos.dto.CustomerDTO;
import lk.ijse.dep7.pos.pos.dto.ItemDTO;
import lk.ijse.dep7.pos.pos.dto.OrderDTO;
import lk.ijse.dep7.pos.pos.dto.OrderDetailDTO;
import lk.ijse.dep7.pos.pos.entity.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class EntityDTOMapper {


    public static CustomerDTO toCustomerDTO(Customer c){
        return new CustomerDTO(c.getId(), c.getName(), c.getAddress());
    }

    public static Customer fromCustomerDTO(CustomerDTO c){
        return  new Customer(c.getId(), c.getName(), c.getAddress());
    }

    public static List<CustomerDTO> toCustomerDTOList(List<Customer> customers){
        return customers.stream().map(c -> toCustomerDTO(c)).collect(Collectors.toList());
    }

    public List<Customer> fromCustomerDTOList(List<CustomerDTO> customerDTOS){
//        return customerDTOS.stream().map(c -> new Customer(c.getId(), c.getName(), c.getAddress())).collect(Collectors.toList());
        return customerDTOS.stream().map(c -> fromCustomerDTO(c)).collect(Collectors.toList());
    }




    public static ItemDTO toItemDTO(Item i){
        return new ItemDTO(i.getCode(), i.getDescription(), i.getUnitPrice(), i.getQtyOnHand());
    }

    public static Item fromItemDTO(ItemDTO i){
        return  new Item(i.getCode(), i.getDescription(), i.getUnitPrice(), i.getQtyOnHand());
    }

    public static List<ItemDTO> toItemDTOList(List<Item> items){
        return items.stream().map(i -> toItemDTO(i)).collect(Collectors.toList());
    }

    public List<Item> fromItemDTOList(List<ItemDTO> itemDTOS){
        return itemDTOS.stream().map(i -> fromItemDTO(i)).collect(Collectors.toList());
    }





    public static Order fromOrderDTO(OrderDTO o){
        return  new Order(o.getOrderId(), Date.valueOf(o.getOrderDate()), o.getCustomerId());
    }

    public static OrderDetail fromOrderDetailDTO(String orderId, OrderDetailDTO od){
        return  new OrderDetail(orderId, od.getItemCode(), od.getUnitPrice(), od.getQty());
    }

    public static OrderDTO toOrderDTO(HashMap<String, Object> or) {
        return new OrderDTO(or.get("id").toString(),
                ((Date)or.get("date")).toLocalDate(),
                or.get("customer_id").toString(),
                or.get("name").toString(),
                (BigDecimal) or.get("total"));
    }

    public static OrderDTO toOrderDTO(CustomEntity ce) {
        return new OrderDTO(ce.getOrderId(),
                ce.getOrderDate().toLocalDate(),
                ce.getCustomerId(),
                ce.getCustomerName(),
                ce.getOrderTotal());
    }

    public static List<OrderDTO> toOrderDTO1(List<HashMap<String, Object>> orderRecords){
        return orderRecords.stream().map(EntityDTOMapper::toOrderDTO).collect(Collectors.toList());
    }

    public static List<OrderDTO> toOrderDTO2(List<CustomEntity> orderRecords){
        return orderRecords.stream().map(EntityDTOMapper::toOrderDTO).collect(Collectors.toList());
    }

}
