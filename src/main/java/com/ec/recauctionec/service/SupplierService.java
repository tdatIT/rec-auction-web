package com.ec.recauctionec.service;

import com.ec.recauctionec.entities.AddressData;
import com.ec.recauctionec.entities.Supplier;
import com.ec.recauctionec.entities.User;

public interface SupplierService {
    Supplier findByOwnerId(int ownerId);

    boolean insertNewSupplier(User user, AddressData address);
}
