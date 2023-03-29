package com.ec.recauctionec.data.dto;

import com.ec.recauctionec.data.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;

@Data
@NoArgsConstructor
public class UserDTO {
    private int userId;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String phoneNumber;
    @NotEmpty
    private String email;
    private int roleId;
    private boolean active;
    private String avatar;
    private int levelUser;
    @Min(value = 8)
    private String password;
    private String username;
    private Date createDate;

    public User mappingClass() {
        User us = new User();
        BeanUtils.copyProperties(this, us);
        return us;
    }

}
