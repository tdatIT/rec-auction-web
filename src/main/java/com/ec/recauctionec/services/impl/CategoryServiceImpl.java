package com.ec.recauctionec.services.impl;

import com.ec.recauctionec.data.entities.Category;
import com.ec.recauctionec.data.repositories.CategoryRepo;
import com.ec.recauctionec.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public List<Category> findAll() {
        return categoryRepo.findAll();
    }
}
