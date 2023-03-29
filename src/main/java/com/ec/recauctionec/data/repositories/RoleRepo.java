package com.ec.recauctionec.data.repositories;

import com.ec.recauctionec.data.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Integer> {
    Role findByRoleId(int id);
}
