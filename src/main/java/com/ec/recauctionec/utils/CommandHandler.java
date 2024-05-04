package com.ec.recauctionec.utils;

import com.ec.recauctionec.data.entities.Role;
import com.ec.recauctionec.data.repositories.RoleRepo;
import com.ec.recauctionec.data.variable.RoleConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class CommandHandler {
    private final RoleRepo roleRepos;

    @PostConstruct
    public void initRolesData() {
        if (roleRepos.findByRoleId(RoleConst.ADMIN) == null) {
            Role role = new Role();
            role.setName(RoleConst.getNameRole(RoleConst.ADMIN));
            role.setDescription("Super Admin");
            roleRepos.save(role);
        }

        if (roleRepos.findByRoleId(RoleConst.SUPPLIER) == null) {
            Role role = new Role();
            role.setName(RoleConst.getNameRole(RoleConst.SUPPLIER));
            role.setDescription("Vendor/Supplier");
            roleRepos.save(role);
        }

        if (roleRepos.findByRoleId(RoleConst.USER) == null) {
            Role role = new Role();
            role.setName(RoleConst.getNameRole(RoleConst.USER));
            role.setDescription("User");
            roleRepos.save(role);
        }


        if (roleRepos.findByRoleId(RoleConst.GUEST) == null) {
            Role role = new Role();
            role.setName(RoleConst.getNameRole(RoleConst.GUEST));
            role.setDescription("Guest");
            roleRepos.save(role);
        }
    }


}
