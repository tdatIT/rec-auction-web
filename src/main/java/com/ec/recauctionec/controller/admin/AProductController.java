package com.ec.recauctionec.controller.admin;

import com.ec.recauctionec.data.entities.Product;
import com.ec.recauctionec.services.CategoryService;
import com.ec.recauctionec.services.ProductService;
import com.ec.recauctionec.services.StorageImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/san-pham")
public class AProductController {
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    StorageImage storageImage;

    @GetMapping(value = {""})
    public String getProductList(@RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer size,
                                 ModelMap modelMap) {
        if (page == null)
            page = 0;
        if (size == null)
            size = 20;
        List<Product> productList = productService.findAllProduct(page, size);
        long total = productService.totalProduct();
        long total_active = productService.totalActive();
        modelMap.addAttribute("products", productList);
        modelMap.addAttribute("total_products", total);
        modelMap.addAttribute("total_active", total_active);
        return "admin/products";
    }

    @RequestMapping(value = "/khoa-san-pham", method = RequestMethod.POST)
    public ResponseEntity updateProduct(@RequestParam String code) {
        Product p = productService.findByProductCode(code);
        p.setStatus(Product.DISABLE);
        p.setUpdateDate(new Date(new java.util.Date().getTime()));
        productService.updateProduct(p);
        return ResponseEntity.status(HttpStatus.OK).body("Ban success");
    }


}
