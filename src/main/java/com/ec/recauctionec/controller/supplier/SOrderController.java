package com.ec.recauctionec.controller.supplier;

import com.ec.recauctionec.data.dto.OrderDTO;
import com.ec.recauctionec.data.entities.CustomUserDetails;
import com.ec.recauctionec.data.entities.Orders;
import com.ec.recauctionec.data.entities.User;
import com.ec.recauctionec.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/supplier/don-hang")
public class SOrderController {

    private final OrderService orderService;
    private Authentication auth;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getOrderList(@RequestParam(name = "date-filter", required = false) java.sql.Date filterDate,
                               @RequestParam(name = "status", required = false) Integer status,
                               ModelMap modelMap) {

        auth = SecurityContextHolder.getContext().getAuthentication();
        User us = ((CustomUserDetails) auth.getPrincipal()).getUser();
        List<Orders> orders;
        if (filterDate == null) {
            filterDate = new Date(new java.util.Date().getTime());
            orders = orderService.find5LastOrderBySupplier(us.getSuppliers());
        } else {
            orders = orderService.findAllBySupplierDate(us.getSuppliers(), filterDate);
        }

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

    @RequestMapping(value = "/cap-nhat", method = RequestMethod.POST)
    public String getOrderInfo(@ModelAttribute OrderDTO dto) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();
        Orders orders = orderService.findById(dto.getOrderId());
        if (orders != null &&
                orders.getProduct().getSupplier().getSupplierId() == user.getSuppliers().getSupplierId()) {
            orders.setStatus(dto.getStatus());
            orders.setUpdatedDate(new Date(new java.util.Date().getTime()));
            orderService.updateOrder(orders);
            return "redirect:/supplier/don-hang";
        }
        return "redirect:/404";
    }
}
