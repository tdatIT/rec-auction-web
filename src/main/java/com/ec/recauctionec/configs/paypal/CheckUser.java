package com.ec.recauctionec.configs.paypal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckUser {
    private int code;
    private String message;
    private boolean status;
}
