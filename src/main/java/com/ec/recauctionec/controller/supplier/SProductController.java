package com.ec.recauctionec.controller.supplier;

import com.ec.recauctionec.dto.ProductDTO;
import com.ec.recauctionec.entities.*;
import com.ec.recauctionec.service.CategoryService;
import com.ec.recauctionec.service.ProductService;
import com.ec.recauctionec.service.StorageImage;
import com.ec.recauctionec.service.SupplierService;
import com.ec.recauctionec.variable.SupplierLevel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
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
        if (SupplierLevel.checkingAvailableProduct(sup)) {
            int available = SupplierLevel.getNumberProductAvailable(sup.getLevelSupp()) - products.size();
            modelMap.addAttribute("message", "Bạn thể thêm  [" + available
                    + "] sản phẩm");
        }
        modelMap.addAttribute("products", products);
        return "supplier/products";
    }

    @RequestMapping(value = "/them", method = RequestMethod.GET)
    public String getInsertProduct(ModelMap modelMap) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();
        Supplier sup = user.getSuppliers();

        if (SupplierLevel.checkingAvailableProduct(sup)) {
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
    public String insertNewProduct(@ModelAttribute ProductDTO productDTO,
                                   ModelMap modelMap) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();
        Supplier sup = user.getSuppliers();

        if (SupplierLevel.checkingAvailableProduct(sup)) {
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


    @RequestMapping(value = "/chinh-sua/{id}", method = RequestMethod.GET)
    public String updateProduct(@PathVariable int id, ModelMap modelMap) {

        List<Category> categories = categoryService.findAll();
        Product p = productService.findById(id);
        ProductDTO productDTO = modelMapper.map(p, ProductDTO.class);

        modelMap.addAttribute("action", "/supplier/san-pham/chinh-sua");
        modelMap.addAttribute("productDTO", productDTO);
        modelMap.addAttribute("categories", categories);
        return "supplier/product-info";
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
        return "redirect:/supplier/san-pham";
    }

    @RequestMapping(value = "/xoa/{id}", method = RequestMethod.GET)
    public String deleteProduct(@PathVariable int id, ModelMap modelMap) {
        Product p = productService.findById(id);
        productService.deleteProduct(p);
        return "redirect:/supplier/san-pham";
    }

}
