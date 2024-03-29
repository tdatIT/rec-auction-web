package com.ec.recauctionec.controller;

import com.ec.recauctionec.data.entities.Bid;
import com.ec.recauctionec.data.entities.Product;
import com.ec.recauctionec.services.BidService;
import com.ec.recauctionec.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/chi-tiet-san-pham")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private BidService bidService;

    @RequestMapping("/{productCode}")
    public String viewProductSupplier(@PathVariable String productCode, ModelMap modelMap) {
        List<Bid> top10Auction = bidService.findTop10AuctionForDay();
        modelMap.addAttribute("top10Auction", top10Auction);
        Product p = productService.findByProductCode(productCode);
        if (p.getProductId() != 0) {
            modelMap.addAttribute("product", p);
            return "product-suppview";
        }
        return "redirect:/404";
    }
}
