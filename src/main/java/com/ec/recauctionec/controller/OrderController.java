package com.ec.recauctionec.controller;

import com.ec.recauctionec.data.entities.CustomUserDetails;
import com.ec.recauctionec.data.entities.Orders;
import com.ec.recauctionec.data.entities.User;
import com.ec.recauctionec.data.repositories.UserAddressRepo;
import com.ec.recauctionec.data.repositories.WalletRepo;
import com.ec.recauctionec.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/don-hang")
public class OrderController {
    private final OrderService orderService;
    private final UserAddressRepo userAddressRepo;
    private final WalletRepo walletRepo;

    @GetMapping("")
    public String getOrderList(@RequestParam(value = "filter", required = false)
                               @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                               ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        List<Orders> orders = date == null ?
                orderService.find5LastOrder(user)
                : orderService.findOrderByDate(user.getUserId(), date);
        modelMap.addAttribute("orders", orders);
        return "user/order-list";
    }

    @GetMapping("/xac-nhan-don-hang/{id}")
    public String getConfirmOrder(@PathVariable("id") int orderId, ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        try {
            Orders order = orderService.findById(orderId);
            if (order != null && order.getUser().getUserId() == user.getUserId()) {
                modelMap.addAttribute("order", order);
                modelMap.addAttribute("address", userAddressRepo.findByUser(user));
                modelMap.addAttribute("balance", walletRepo
                        .findByUserId(user.getUserId()).iterator().next()
                        .getAccountBalance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "checkout";
    }
}