package com.ec.recauctionec.data.repositories;


import com.ec.recauctionec.data.entities.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Integer> {
    @Query("select p from Product p where p.supplier.supplierId = ?1 and p.isDeleted=false")
    List<Product> findAllBySupplierIdActive(int supplierId);

    @Query("select p from  Product p where p.productName like %:name%")
    List<Product> findAllByProductName(String name);

    @Query("select p from Product p order by p.updateDate desc")
    List<Product> findAllProductByPageAndSize(Pageable pageable);

    @Query("select count(p) from Product p")
    long totalProduct();

    @Query("select count(p) from Product p where p.status <>0")
    long totalActive();

    Product findByProductId(int productId);

    Product findProductByProductCode(String productCode);

    List<Product> findAllByStatus(int status);

    @Query("select p from Product p where p.status = 1 or p.status = 2 order by p.defaultPrice desc")
    List<Product> findProductLimit(Pageable pageable);


    @Query("select p from Product p where p.category.categoryId = ?1")
    List<Product> findByCategoryId(int categoryId);

    @Query("select p from Product p " +
            "where p.supplier.user.userId=:userId" +
            " and p.productTag like %:productTagStr%")
    List<Product> findProductForAuction(@RequestParam("userId") int userId, @RequestParam("productTag") String productTagStr);


}
