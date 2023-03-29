package com.ec.recauctionec.controller.admin;

import com.ec.recauctionec.data.entities.Orders;
import com.ec.recauctionec.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/don-hang")
public class AOrderController {
    @Autowired
    OrderService orderService;

    @GetMapping(value = {""})
    public String getProductList(@RequestParam(required = false) Integer page,
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

}
