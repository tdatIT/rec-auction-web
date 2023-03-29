package com.ec.recauctionec.data.repositories;

import com.ec.recauctionec.data.entities.Commission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommissionRepo extends JpaRepository<Commission, Integer> {

}
