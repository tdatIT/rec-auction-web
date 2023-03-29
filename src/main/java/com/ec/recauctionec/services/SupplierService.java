package com.ec.recauctionec.services;

import com.ec.recauctionec.data.entities.AddressData;
import com.ec.recauctionec.data.entities.Supplier;
import com.ec.recauctionec.data.entities.User;

public interface SupplierService {
    Supplier findByOwnerId(int ownerId);

    boolean insertNewSupplier(User user, AddressData address);
}
