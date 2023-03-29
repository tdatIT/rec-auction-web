package com.ec.recauctionec.services;

import com.ec.recauctionec.data.entities.Product;

import java.util.List;

public interface ProductService {
    List<Product> findBySupplierId(int supplierId);

    List<Product> findByName(String name);

    Product findById(int id);

    Product findByProductCode(String code);

    void insertProduct(Product product);

    void updateStatusProduct(Product product, int status);

    void updateProduct(Product product);

    void deleteProduct(Product product);

    List<Product> findTop5Trending();

    List<Product> findByCategoryId(int categoryId);

    List<Product> findProductForAuction(int userId, String productTagStr);

    List<Product> findAllProduct(int page, int size);

    long totalProduct();

    long totalActive();
}
