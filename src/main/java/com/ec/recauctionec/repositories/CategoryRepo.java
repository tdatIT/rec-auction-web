package com.ec.recauctionec.repositories;

import com.ec.recauctionec.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
    List<Category> findAll();

}
