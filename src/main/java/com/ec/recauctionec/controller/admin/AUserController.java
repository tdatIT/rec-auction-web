package com.ec.recauctionec.controller.admin;

import com.ec.recauctionec.entities.User;
import com.ec.recauctionec.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/nguoi-dung")
public class AUserController {
    @Autowired
    UserService userService;

    @GetMapping(value = {""})
    public String getUserList(@RequestParam(required = false) Integer page,
                              @RequestParam(required = false) Integer size,
                              ModelMap modelMap) {
        if (page == null)
            page = 0;
        if (size == null)
            size = 20;
        List<User> userList = userService.findAllUser(page, size);
        long total_user = userService.totalUserInDB();
        long total_supplier = userService.totalSupplierInDB();
        long total_admin = userService.totalAdminInDB();
        modelMap.addAttribute("total_user", total_user);
        modelMap.addAttribute("total_supplier", total_supplier);
        modelMap.addAttribute("total_admin", total_admin);
        modelMap.addAttribute("userList", userList);
        return "admin/users";
    }

    @RequestMapping(value = {"/chinh-sua/{id}"}, method = RequestMethod.GET)
    public String disableUser(@PathVariable int id, ModelMap modelMap) {
        User user = userService.findById(id);
        if (user.isActive() == true) {
            user.setActive(false);
        } else
            user.setActive(true);
        userService.updateUser(user);
        return "redirect:/admin/nguoi-dung";

    }
}
