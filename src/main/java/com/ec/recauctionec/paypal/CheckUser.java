package com.ec.recauctionec.paypal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckUser {
    private int code;
    private String message;
    private boolean status;
}
