package com.ec.recauctionec.controller;

import com.ec.recauctionec.data.entities.Product;
import com.ec.recauctionec.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {
    private final ProductService productService;

    @GetMapping("/tim-kiem")
    public String getSearchPage(@RequestParam("keyword") String keyword, ModelMap modelMap) {
        if (isNumeric(keyword)) {
            int id = Integer.parseInt(keyword);
            return "redirect:/chi-tiet-dau-gia/" + id;
        } else {
            List<Product> products = productService.findByName(keyword);
            modelMap.addAttribute("products", products);
            return "category-view";
        }
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
}
