package com.ec.recauctionec.repositories;

import com.ec.recauctionec.entities.User;
import com.ec.recauctionec.verification.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);
}
