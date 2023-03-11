package com.ec.recauctionec.repositories;


import com.ec.recauctionec.entities.Product;
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

    Product findByProductId(int productId);

    Product findProductByProductCode(String productCode);

    List<Product> findAllByStatus(int status);

    @Query("select p from Product p where p.status = 1 or p.status = 2 order by p.defaultPrice desc")
    List<Product> findProductLimit(Pageable pageable);

    @Query("select p from Product p where p.isDeleted = false ")
    List<Product> findAllActive();

    @Query("select p from Product p where p.category.categoryId = ?1")
    List<Product> findByCategoryId(int categoryId);

    @Query("select p from Product p " +
            "where p.supplier.user.userId=:userId" +
            " and p.productTag like %:productTagStr%")
    List<Product> findProductForAuction(@RequestParam("userId") int userId, @RequestParam("productTag") String productTagStr);


}
