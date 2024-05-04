package com.ec.recauctionec.controller.admin;

import com.ec.recauctionec.data.entities.User;
import com.ec.recauctionec.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/nguoi-dung")
public class AUserController {
    final UserService userService;

    @GetMapping(value = {""})
    public String getUserList(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, ModelMap modelMap) {
        if (page == null) page = 0;
        if (size == null) size = 20;
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

    @RequestMapping(value = {"/khoa-tai-khoan"}, method = RequestMethod.POST)
    public ResponseEntity disableUser(@RequestParam String email) {
        User user = userService.findByEmail(email);
        if (user.isActive() == true) {
            user.setActive(false);
            userService.updateUser(user);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Ban account has been success");
    }
}
