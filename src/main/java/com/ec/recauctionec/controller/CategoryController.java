package com.ec.recauctionec.controller;

import com.ec.recauctionec.data.entities.Product;
import com.ec.recauctionec.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/danh-muc-san-pham")
public class CategoryController {
    private final ProductService productService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getProduct(@RequestParam("id") int id,
                             ModelMap modelMap) {
        List<Product> products = productService.findByCategoryId(id);
        modelMap.addAttribute("products", products);
        return "category-view";
    }
}
