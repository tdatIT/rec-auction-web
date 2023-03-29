package com.ec.recauctionec.services.impl;

import com.ec.recauctionec.data.entities.ProductTag;
import com.ec.recauctionec.data.repositories.ProductTagRepo;
import com.ec.recauctionec.services.ProductTagService;
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
