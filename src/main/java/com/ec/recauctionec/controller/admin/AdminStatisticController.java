package com.ec.recauctionec.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminStatisticController {
    @GetMapping(value = {"", "/phan-tich"})
    public String getDashboard(ModelMap modelMap) {
        return "admin/blank-page";
    }
}
