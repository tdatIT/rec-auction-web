package com.ec.recauctionec.service;

import com.ec.recauctionec.entities.Product;

import java.util.List;

public interface ProductService {
    List<Product> findBySupplierId(int supplierId);

    List<Product> findByName(String name);

    Product findById(int id);

    void insertProduct(Product product);

    void updateStatusProduct(Product product, int status);

    void updateProduct(Product product);
    void deleteProduct(Product product);

    List<Product> findTop5Trending();
    List<Product> findByCategoryId(int categoryId);

    List<Product> findProductForAuction(int userId,String productTagStr);
    List<Product> findAllProduct();
}
