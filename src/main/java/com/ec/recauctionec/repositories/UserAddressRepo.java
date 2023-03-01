package com.ec.recauctionec.repositories;

import com.ec.recauctionec.entities.User;
import com.ec.recauctionec.entities.AddressData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAddressRepo extends JpaRepository<AddressData, Integer> {
    List<AddressData> findByUser(User user);
}
