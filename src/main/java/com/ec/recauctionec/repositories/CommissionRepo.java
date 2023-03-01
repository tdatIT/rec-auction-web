package com.ec.recauctionec.repositories;

import com.ec.recauctionec.entities.Commission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommissionRepo extends JpaRepository<Commission, Integer> {

}
