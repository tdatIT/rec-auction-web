package com.ec.recauctionec.data.repositories;

import com.ec.recauctionec.data.entities.AddressData;
import com.ec.recauctionec.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAddressRepo extends JpaRepository<AddressData, Integer> {
    List<AddressData> findByUser(User user);
}
