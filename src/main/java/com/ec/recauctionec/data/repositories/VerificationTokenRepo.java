package com.ec.recauctionec.data.repositories;

import com.ec.recauctionec.data.entities.User;
import com.ec.recauctionec.data.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);
}
