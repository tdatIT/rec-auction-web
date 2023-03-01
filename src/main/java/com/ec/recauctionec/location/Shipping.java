package com.ec.recauctionec.location;

import com.ec.recauctionec.entities.AddressData;
import com.ec.recauctionec.entities.Delivery;


public class Shipping {
    public static float IN_CITY = 1;
    public static float OUT_CITY = 1.5F;
    public static float DIFF = 2.0F;

    public static float calculateShipping(AddressData src, AddressData des, Delivery deli) {
        if (src.getProvince() != des.getProvince())
            return DIFF * deli.getDiscount();
        if (src.getDistrict() != des.getDistrict())
            return OUT_CITY * deli.getDiscount();
        return IN_CITY * deli.getDiscount();
    }

}
