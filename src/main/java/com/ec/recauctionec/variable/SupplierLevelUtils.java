package com.ec.recauctionec.variable;

import com.ec.recauctionec.entities.Supplier;

public class SupplierLevelUtils {

    public static boolean checkingAvailableProduct(Supplier supplier) {
        int noProduct = supplier.getProducts().size();
        switch (supplier.getLevelSupp()) {
            case Supplier.LEVEL_BASIC:
                return (noProduct < Supplier.NUM_OF_PRODUCTS_FOR_BASIC ? true : false);
            case Supplier.LEVEL_PREMIUM:
                return (noProduct < Supplier.NUM_OF_PRODUCTS_FOR_PREMIUM ? true : false);
            case Supplier.LEVEL_MALL:
                return (noProduct < Supplier.NUM_OF_PRODUCTS_FOR_MALL ? true : false);
        }
        return false;

    }

    public static int getNumberProductAvailable(int supplier_level) {
        switch (supplier_level) {
            case Supplier.LEVEL_BASIC:
                return Supplier.NUM_OF_PRODUCTS_FOR_BASIC;
            case Supplier.LEVEL_PREMIUM:
                return Supplier.NUM_OF_PRODUCTS_FOR_PREMIUM;
            case Supplier.LEVEL_MALL:
                return Supplier.NUM_OF_PRODUCTS_FOR_MALL;
        }
        return -1;
    }
}
