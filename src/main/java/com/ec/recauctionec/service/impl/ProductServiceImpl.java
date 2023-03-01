package com.ec.recauctionec.service.impl;

import com.ec.recauctionec.entities.Product;
import com.ec.recauctionec.repositories.ProductRepo;
import com.ec.recauctionec.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepo productRepo;

    @Override
    public List<Product> findBySupplierId(int supplierId) {
        return productRepo.findAllBySupplierIdActive(supplierId);
    }

    @Override
    public List<Product> findByName(String name) {
        return productRepo.findAllByProductName(name);
    }

    @Override
    public Product findById(int id) {
        return productRepo.findById(id).orElseThrow();
    }

    @Override
    public void insertProduct(Product product) {
        productRepo.save(product);
    }

    @Override
    public void updateStatusProduct(Product product, int status) {
        product.setStatus(status);
        productRepo.save(product);
    }

    @Override
    public void updateProduct(Product product) {
        productRepo.save(product);
    }

    @Override
    public void deleteProduct(Product product) {
        product.setDeleted(true);
        productRepo.save(product);
    }

    @Override
    public List<Product> findTop5Trending() {
        Pageable top5 = PageRequest.of(0, 5);
        return productRepo.findProductLimit(top5);
    }

    @Override
    public List<Product> findProductForAuction(int userId, String productTagStr) {
        return productRepo.findProductForAuction(userId, productTagStr);
    }

    @Override
    public List<Product> findByCategoryId(int categoryId) {
        return productRepo.findByCategoryId(categoryId);
    }

    @Override
    public List<Product> findAllProduct() {
        return productRepo.findAllActive();
    }
}
