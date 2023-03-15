package com.ec.recauctionec.controller.supplier;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/supplier")

public class SupplierController {

    @RequestMapping(value = {""})
    public String getDashboard(ModelMap modelMap) {
        return "redirect:/supplier/san-pham";
    }
}
