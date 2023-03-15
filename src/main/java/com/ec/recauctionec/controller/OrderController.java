package com.ec.recauctionec.controller;

import com.ec.recauctionec.dto.OrderDTO;
import com.ec.recauctionec.entities.AddressData;
import com.ec.recauctionec.entities.CustomUserDetails;
import com.ec.recauctionec.entities.Orders;
import com.ec.recauctionec.entities.User;
import com.ec.recauctionec.repositories.UserAddressRepo;
import com.ec.recauctionec.repositories.WalletRepo;
import com.ec.recauctionec.services.OrderService;
import org.springframework.beans.BeanUtils;
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
@RequestMapping("/don-hang")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserAddressRepo userAddressRepo;
    @Autowired
    private WalletRepo walletRepo;

    @GetMapping("")
    public String getOrderList(@RequestParam(value = "filter", required = false)
                               @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                               ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        List<Orders> orders = date == null ?
                orderService.findOrderByDate(user.getUserId(), new Date(new Date().getTime()))
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

    @PostMapping("/xac-nhan-don-hang")
    public String confirmOrder(@RequestParam("orderId") int orderId,
                               @RequestParam("addressId") int addressId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        try {
            Orders order = orderService.findById(orderId);
            if (order != null &&
                    order.getUser().getUserId() == user.getUserId()) {

                AddressData address = userAddressRepo.findById(addressId)
                        .orElseThrow();
                OrderDTO dto = new OrderDTO();
                BeanUtils.copyProperties(order, dto);
                dto.setAddress(address);
                orderService.confirmOrder(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/don-hang";
    }
}