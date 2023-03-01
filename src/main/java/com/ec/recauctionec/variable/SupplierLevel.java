package com.ec.recauctionec.variable;

import com.ec.recauctionec.entities.Supplier;

public class SupplierLevel {
    public static final int STANDDARD = 1;
    public static final int PREMIUM = 2;
    public static final int NO_LIMIT = 3;

    private static final int ST_PRODUCT = 5;
    private static final int PRE_PRODUCT = 10;
    private static final int NOL_PRODUCT = 20;

    public static boolean checkingAvailableProduct(Supplier supplier) {
        int noProduct = supplier.getProducts().size();
        switch (supplier.getLevelSupp()) {
            case STANDDARD:
                return (noProduct < ST_PRODUCT ? true : false);
            case PREMIUM:
                return (noProduct < PRE_PRODUCT ? true : false);
            case NO_LIMIT:
                return (noProduct < NOL_PRODUCT ? true : false);
        }
        return false;

    }

    public static int getNumberProductAvailable(int supplier_level) {
        switch (supplier_level) {
            case STANDDARD:
                return ST_PRODUCT;
            case PREMIUM:
                return PRE_PRODUCT;
            case NO_LIMIT:
                return NOL_PRODUCT;
        }
        return -1;
    }
}
