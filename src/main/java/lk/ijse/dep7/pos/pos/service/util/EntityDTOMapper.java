package lk.ijse.dep7.pos.pos.service.util;

import lk.ijse.dep7.pos.pos.dto.CustomerDTO;
import lk.ijse.dep7.pos.pos.entity.Customer;

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
}
