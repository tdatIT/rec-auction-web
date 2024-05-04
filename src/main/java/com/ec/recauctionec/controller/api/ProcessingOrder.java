package com.ec.recauctionec.controller.api;

import com.ec.recauctionec.data.dto.OrderDTO;
import com.ec.recauctionec.data.entities.AddressData;
import com.ec.recauctionec.data.entities.CustomUserDetails;
import com.ec.recauctionec.data.entities.Orders;
import com.ec.recauctionec.data.entities.User;
import com.ec.recauctionec.data.repositories.UserAddressRepo;
import com.ec.recauctionec.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProcessingOrder {

    private final OrderService orderService;
    private final UserAddressRepo userAddressRepo;

    Authentication auth;

    @RequestMapping(value = "/xac-nhan-don-hang", method = RequestMethod.POST)
    public ResponseEntity confirmOrder(@RequestParam("orderId") int orderId,
                                       @RequestParam("addressId") int addressId) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();

        Orders order = orderService.findById(orderId);
        if (order != null &&
                order.getUser().getUserId() == user.getUserId()) {

            AddressData address = userAddressRepo.findById(addressId)
                    .orElseThrow();
            OrderDTO dto = new OrderDTO();
            BeanUtils.copyProperties(order, dto);
            dto.setAddress(address);
            if (orderService.confirmOrder(dto))
                return ResponseEntity.ok("Create new order success");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Create order was failed, cause wallet not enough");
    }

    @RequestMapping(value = "/huy-don-hang/{orderId}", method = RequestMethod.POST)
    public ResponseEntity cancelOrder(@PathVariable Integer orderId) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();
        Orders order = orderService.findById(orderId);
        if (order != null &&
                order.getUser().getUserId() == user.getUserId()) {
            if (orderService.cancelOrder(orderId))
                return ResponseEntity.status(HttpStatus.OK).body("Cancel order has been successful");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cancel was failed. Check wallet balance again !");
    }
}

