package com.ec.recauctionec.services.shipping;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShipCostObject {
    private String message;
    private int cost;
}
