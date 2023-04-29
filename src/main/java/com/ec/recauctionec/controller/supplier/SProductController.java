package com.ec.recauctionec.controller.supplier;

import com.ec.recauctionec.data.dto.ProductDTO;
import com.ec.recauctionec.data.entities.*;
import com.ec.recauctionec.data.variable.SupplierLevelUtils;
import com.ec.recauctionec.services.CategoryService;
import com.ec.recauctionec.services.ProductService;
import com.ec.recauctionec.services.StorageImage;
import com.ec.recauctionec.services.SupplierService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/supplier/san-pham")
public class SProductController {
    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    SupplierService supplierService;

    @Autowired
    StorageImage storageImage;

    @Autowired
    ModelMapper modelMapper;

    private Authentication auth;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getProductList(ModelMap modelMap, HttpServletRequest request) {
        //get user session and supplier from user
        auth = SecurityContextHolder.getContext().getAuthentication();
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();
        Supplier sup = user.getSuppliers();
        //get product list
        List<Product> products = productService.findBySupplierId(sup.getSupplierId());
        modelMap.addAttribute("products", products);
        //get number of products available
        int max_product = SupplierLevelUtils.getNumberProductAvailable(sup.getLevelSupp());
        boolean available_product = SupplierLevelUtils.checkingAvailableProduct(sup);
        modelMap.addAttribute("enable_add", available_product);
        modelMap.addAttribute("total_products", max_product);
        return "supplier/products";
    }

    @RequestMapping(value = "/them", method = RequestMethod.GET)
    public String getInsertProduct(ModelMap modelMap) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();
        Supplier sup = user.getSuppliers();

        if (SupplierLevelUtils.checkingAvailableProduct(sup)) {
            List<Category> categories = categoryService.findAll();
            ProductDTO productDTO = new ProductDTO();
            modelMap.addAttribute("categories", categories);
            modelMap.addAttribute("productDTO", productDTO);
            modelMap.addAttribute("action", "them");
            return "supplier/product-form";
        }
        return "forward:/supplier/san-pham";
    }


    @RequestMapping(value = "/them", method = RequestMethod.POST)
    public String insertNewProduct(@ModelAttribute ProductDTO productDTO) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();
        Supplier sup = user.getSuppliers();

        if (SupplierLevelUtils.checkingAvailableProduct(sup)) {
            Product product = modelMapper.map(productDTO, Product.class);
            product.setSupplier(sup);
            List<String> filenames = storageImage.storageMultiImage(productDTO.getImages_file());
            product.setImages(filenames.stream().map(
                    t -> {
                        ProductImg img = new ProductImg();
                        img.setImgName(t);
                        img.setProduct(product);
                        return img;
                    }).collect(Collectors.toList()));
            productService.insertProduct(product);
        }
        return "redirect:/supplier/san-pham";

    }


    @RequestMapping(value = "/chinh-sua/{productCode}", method = RequestMethod.GET)
    public String updateProduct(@PathVariable String productCode, ModelMap modelMap) {

        List<Category> categories = categoryService.findAll();
        Product p = productService.findByProductCode(productCode);
        ProductDTO productDTO = modelMapper.map(p, ProductDTO.class);
        modelMap.addAttribute("action", "/supplier/san-pham/chinh-sua");
        modelMap.addAttribute("productDTO", productDTO);
        modelMap.addAttribute("categories", categories);
        return "supplier/product-form";
    }

    @RequestMapping(value = "/chinh-sua", method = RequestMethod.POST)
    public String updateProduct(@ModelAttribute ProductDTO productDTO) {
        Product p = productService.findByProductCode(productDTO.getProductCode());
        BeanUtils.copyProperties(productDTO, p);
        p.setUpdateDate(new Date());
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
        return "redirect:/supplier/san-pham";
    }

    @RequestMapping(value = "/xoa", method = RequestMethod.POST)
    public ResponseEntity deleteProduct(@RequestParam String productCode) {
        Product p = productService.findByProductCode(productCode);
        productService.deleteProduct(p);
        return ResponseEntity.status(HttpStatus.OK).body("Delete product success");
    }

}
