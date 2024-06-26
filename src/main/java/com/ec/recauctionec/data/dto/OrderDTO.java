package com.ec.recauctionec.data.dto;

import com.ec.recauctionec.data.entities.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@EqualsAndHashCode
public class OrderDTO {

    private int orderId;
    @NotNull
    private Timestamp createdDate;

    private Date updatedDate;

    private int deliveryId;

    private double shippingPrice;
    @NotNull
    private int status;
    @NotNull
    private double totalPrice;

    private BidParticipant winAuction;

    private Delivery deliveryByDeliveryId;

    private Product product;

    private AddressData address;
    @NotNull
    private User user;

    private Commission commission;

    public Orders mapping() {
        Orders order = new Orders();
        BeanUtils.copyProperties(this, order);
        return order;
    }
}
