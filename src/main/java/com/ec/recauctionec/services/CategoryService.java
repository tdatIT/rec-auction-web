package com.ec.recauctionec.services;

import com.ec.recauctionec.data.entities.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
}
