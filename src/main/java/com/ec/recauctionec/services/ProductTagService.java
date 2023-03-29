package com.ec.recauctionec.services;

import com.ec.recauctionec.data.entities.ProductTag;

import java.util.List;

public interface ProductTagService {
    List<ProductTag> findAll();
}
