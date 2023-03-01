package com.ec.recauctionec.repositories;

import com.ec.recauctionec.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Integer> {
    Role findByRoleId(int id);
}
