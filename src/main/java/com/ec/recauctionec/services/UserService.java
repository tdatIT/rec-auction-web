package com.ec.recauctionec.services;

import com.ec.recauctionec.entities.User;
import com.ec.recauctionec.verification.VerificationToken;

import java.util.List;


public interface UserService {
    User findById(int id);
    User findByUsername(String username);

    User findByEmail(String email);

    User registerAccount(User users);

    void updateUser(User user);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    void requestResetPassword(User us);

    void resetPassword(String token, String password);
    void updateConfirmUser(User user);
    boolean updatePassword(User user, String curPass, String newPass);
    List<User> findAllUser();
}
