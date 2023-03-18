package com.ec.recauctionec.controller.supplier;

import com.ec.recauctionec.entities.CustomUserDetails;
import com.ec.recauctionec.entities.Orders;
import com.ec.recauctionec.entities.User;
import com.ec.recauctionec.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/supplier/don-hang")
public class SOrderController {
    @Autowired
    private OrderService orderService;
    private Authentication auth;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getOrderList(@RequestParam(name = "date-filter", required = false) java.sql.Date filterDate,
                               @RequestParam(name = "status", required = false) Integer status,
                               ModelMap modelMap) {
        if (filterDate == null)
            filterDate = new Date(new java.util.Date().getTime());
        auth = SecurityContextHolder.getContext().getAuthentication();
        User us = ((CustomUserDetails) auth.getPrincipal()).getUser();
        List<Orders> orders = orderService.findAllBySupplierDate(us.getSuppliers(), filterDate);
        if (status != null) {
            orders = orders.stream()
                    .filter(t -> t.getStatus() == status)
                    .collect(Collectors.toList());
        }
        long total_orders = orderService.numberOfOrdersOfSupplier(us.getSuppliers(), filterDate);
        long pending_orders = orderService.numberOfOrdersPendingOfSupplier(us.getSuppliers(), filterDate);

        modelMap.addAttribute("filter_date", filterDate);
        modelMap.addAttribute("total", total_orders);
        modelMap.addAttribute("pending", pending_orders);
        modelMap.addAttribute("orders", orders);

        return "supplier/orders";
    }

    @RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
    public String getOrderInfo(@PathVariable int orderId, ModelMap modelMap) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();
        Orders orders = orderService.findById(orderId);
        if (orders != null &&
                orders.getProduct().getSupplier().getSupplierId() == user.getSuppliers().getSupplierId()) {
            modelMap.addAttribute("order", orders);
            return "supplier/order-form";
        }
        return "redirect:/404";
    }
}