package com.ec.recauctionec.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CustomErrorController implements ErrorController {
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String getPage403(ModelMap modelMap) {
        modelMap.addAttribute("message", "Lỗi 403");
        modelMap.addAttribute("description", "Tài khoản của bạn không có quyền truy cập " +
                "trang web này");
        modelMap.addAttribute("title","Lỗi 403");
        return "message";
    }
    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public String getPage404(ModelMap modelMap) {
        modelMap.addAttribute("message", "Lỗi 404");
        modelMap.addAttribute("description", "Nội dung bạn yêu cầu không khả dụng");
        modelMap.addAttribute("title","Lỗi 404");
        return "message";
    }
}
