package com.ec.recauctionec.service.impl;

import com.ec.recauctionec.entities.ProductTag;
import com.ec.recauctionec.repositories.ProductTagRepo;
import com.ec.recauctionec.service.ProductTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductTagServiceImpl implements ProductTagService {
    @Autowired
    private ProductTagRepo productTagRepo;

    @Override
    public List<ProductTag> findAll() {
        return productTagRepo.findAll();
    }
}
