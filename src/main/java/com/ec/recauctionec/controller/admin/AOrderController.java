package com.ec.recauctionec.controller.admin;

import com.ec.recauctionec.data.dto.OrderDTO;
import com.ec.recauctionec.data.entities.Orders;
import com.ec.recauctionec.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/don-hang")
public class AOrderController {

    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @GetMapping(value = {""})
    public String getOrderList(@RequestParam(required = false) Integer page,
                               @RequestParam(required = false) Integer size,
                               @RequestParam(required = false, name = "date-filter") Date filter,
                               ModelMap modelMap) {
        if (filter == null)
            filter = new Date(new java.util.Date().getTime());
        if (page == null)
            page = 0;
        if (size == null)
            size = 20;
        long total_order = orderService.totalOrderFromDate(filter);
        List<Orders> ordersList = orderService.findAllOrderByDate(filter, page, size);
        modelMap.addAttribute("orders", ordersList);
        modelMap.addAttribute("filter_date", filter);
        modelMap.addAttribute("total_order", total_order);
        return "admin/orders";
    }

    @GetMapping("/{id}")
    public String getOrderDetail(@PathVariable int id, ModelMap modelMap) {
        Orders orders = orderService.findById(id);
        if (orders != null) {
            modelMap.addAttribute("order", orders);
            return "admin/order-form";
        } else
            return "redirect:/404";
    }

    @PostMapping("/cap-nhat")
    public String completeOrder(@ModelAttribute OrderDTO dto) {
        if (dto.getStatus() == Orders.COMPLETE) {
            orderService.completedOrder(dto);
        } else {
            Orders order = modelMapper.map(dto, Orders.class);
            orderService.updateOrder(order);
        }
        return "redirect:/admin/don-hang";
    }

}
