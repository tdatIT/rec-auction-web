
package com.ec.recauctionec.data.repositories;

import com.ec.recauctionec.data.entities.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductTagRepo extends JpaRepository<ProductTag,Long> {
    @Override
    List<ProductTag> findAll();
}
