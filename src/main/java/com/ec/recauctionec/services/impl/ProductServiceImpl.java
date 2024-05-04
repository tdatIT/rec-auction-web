package com.ec.recauctionec.services.impl;

import com.ec.recauctionec.data.entities.Product;
import com.ec.recauctionec.data.repositories.ProductRepo;
import com.ec.recauctionec.data.response.BestSellerQuery;
import com.ec.recauctionec.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;

    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public List<Product> findAllProduct(int page, int size) {
        return productRepo.findAllProductByPageAndSize(PageRequest.of(page, size));
    }

    @Override
    public List<BestSellerQuery> getTopSeller(Integer month, Integer year) {
        return productRepo.getBestSellerInMonth(month,year);
    }

    @Override
    public long totalProduct() {
        return productRepo.totalProduct();
    }

    @Override
    public long totalActive() {
        return productRepo.totalActive();
    }

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
    public Product findByProductCode(String code) {
        return productRepo.findProductByProductCode(code);
    }

    @Override
    public void insertProduct(Product product) {
        String uuid = UUID.randomUUID().toString();
        String productCode = uuid.substring(0, 4)
                + (new Random().nextInt(9999) + 1000);
        product.setUpdatedDate(new Date());
        product.setProductCode(productCode);
        productRepo.save(product);
    }

    @Override
    public void updateStatusProduct(Product product, int status) {
        product.setUpdatedDate(new Date());
        product.setStatus(status);
        productRepo.save(product);

    }

    @Override
    public void updateProduct(Product product) {
        productRepo.save(product);
    }

    @Override
    public void deleteProduct(Product product) {
        product.setUpdatedDate(new Date());
        product.setStatus(Product.DELETED_S);
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


}
