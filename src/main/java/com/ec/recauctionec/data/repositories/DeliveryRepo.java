package com.ec.recauctionec.data.repositories;

import com.ec.recauctionec.data.entities.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepo extends JpaRepository<Delivery,Integer> {
}
