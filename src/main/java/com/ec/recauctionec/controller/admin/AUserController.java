package com.ec.recauctionec.controller.admin;

import com.ec.recauctionec.entities.User;
import com.ec.recauctionec.services.UserService;
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
@RequestMapping("/admin/nguoi-dung")
public class AUserController {
    @Autowired
    UserService userService;

    @GetMapping(value = {""})
    public String getUserList(ModelMap modelMap, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<User> userList = userService.findAllUser();
        modelMap.addAttribute("userList", userList);

        return "admin/admin-user";
    }

    @RequestMapping(value = {"/chinh-sua/{id}"}, method = RequestMethod.GET)
    public String disableUser(@PathVariable int id, ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findById(id);
        if (user.isActive() == true) {
            user.setActive(false);
        } else
            user.setActive(true);
        userService.updateUser(user);
        return "redirect:/admin/nguoi-dung";

    }
}
