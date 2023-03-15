package com.ec.recauctionec.controller.admin;

import com.ec.recauctionec.dto.ProductDTO;
import com.ec.recauctionec.entities.Category;
import com.ec.recauctionec.entities.Product;
import com.ec.recauctionec.entities.ProductImg;
import com.ec.recauctionec.services.CategoryService;
import com.ec.recauctionec.services.ProductService;
import com.ec.recauctionec.services.StorageImage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

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
    public String getProductList(ModelMap modelMap, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Product> productList = productService.findAllProduct();
        modelMap.addAttribute("productList", productList);
        return "admin/admin-product";
    }

    @RequestMapping(value = "/chinh-sua/{id}", method = RequestMethod.GET)
    public String updateProduct(@PathVariable int id, ModelMap modelMap) {

        List<Category> categories = categoryService.findAll();
        Product p = productService.findById(id);
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(p, productDTO);
        modelMap.addAttribute("action", "/admin/san-pham/chinh-sua");
        modelMap.addAttribute("productDTO", productDTO);
        modelMap.addAttribute("categories", categories);
        return "/admin/product-info";
    }

    @Transactional
    @RequestMapping(value = "/chinh-sua", method = RequestMethod.POST)
    public String updateProduct(@ModelAttribute ProductDTO productDTO,
                                ModelMap modelMap) {

        Product p = productService.findById(productDTO.getProductId());
        BeanUtils.copyProperties(productDTO, p);
        if (productDTO.getImages_file() != null) {
            List<String> filenames = storageImage.storageMultiImage(productDTO.getImages_file());
            p.setImages(filenames.stream().map(
                    t -> {
                        ProductImg img = new ProductImg();
                        img.setProduct(p);
                        img.setImgName(t);
                        return img;
                    }
            ).collect(Collectors.toList()));
        }

        productService.updateProduct(p);
        return "redirect:/admin/san-pham";
    }

    @RequestMapping(value = "/xoa/{id}", method = RequestMethod.GET)
    public String deleteProduct(@PathVariable int id, ModelMap modelMap) {
        Product p = productService.findById(id);
        productService.deleteProduct(p);
        return "redirect:/admin/san-pham";
    }

}
