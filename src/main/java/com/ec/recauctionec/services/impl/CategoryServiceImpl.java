package com.ec.recauctionec.services.impl;

import com.ec.recauctionec.data.entities.Category;
import com.ec.recauctionec.data.repositories.CategoryRepo;
import com.ec.recauctionec.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    @Override
    public List<Category> findAll() {
        return categoryRepo.findAll();
    }
}
