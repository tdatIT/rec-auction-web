package com.ec.recauctionec.controller.admin;

import com.ec.recauctionec.entities.Orders;
import com.ec.recauctionec.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin/don-hang")
public class AOrderController {
    @Autowired
    OrderService orderService;

    @GetMapping(value = {""})
    public String getProductList(ModelMap modelMap, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Orders> ordersList = orderService.findAll();
        modelMap.addAttribute("ordersList", ordersList);
        return "admin/admin-order";
    }

    //    @RequestMapping(value = {"/chinh-sua/{id}"}, method = RequestMethod.GET)
//    public String disableUser(@PathVariable int id, ModelMap modelMap) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Orders orders = orderService.findById(id);
//        if (orders.getStatus() == 2 || orders.getStatus() == 3)
//            orderService.completedOrder(orders);
//
//        return "redirect:/admin/don-hang";
//    }
    @RequestMapping(value = {"/chinh-sua/{id}"}, method = RequestMethod.GET)
    public String disableUser(@PathVariable int id, ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Orders orders = orderService.findById(id);
        if (orders.getStatus() == 2 || orders.getStatus() == 3)
            orders.setStatus(4);
        orderService.updateOrder(orders);
        return "redirect:/admin/don-hang";
    }
}
