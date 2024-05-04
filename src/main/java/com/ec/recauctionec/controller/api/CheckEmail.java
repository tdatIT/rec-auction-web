package com.ec.recauctionec.controller.api;

import com.ec.recauctionec.configs.paypal.CheckUser;
import com.ec.recauctionec.data.entities.User;
import com.ec.recauctionec.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CheckEmail {

    private final UserService userService;

    @GetMapping(value = "/check-email")
    public ResponseEntity checkExistEmail(@RequestParam String email) {
        User us = userService.findByEmail(email);
        if (us != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new CheckUser(200,
                            "Email already register for diff account",
                            false));

        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new CheckUser(200,
                        "Email can register new account",
                        true));

    }
}
