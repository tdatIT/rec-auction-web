package com.ec.recauctionec.repositories;

import com.ec.recauctionec.entities.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepo extends JpaRepository<Delivery,Integer> {
}
