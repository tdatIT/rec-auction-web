package com.ec.recauctionec.data.variable;

import com.ec.recauctionec.data.entities.Supplier;

public class SupplierLevelUtils {

    public static boolean checkingAvailableProduct(Supplier supplier) {
        int noProduct = supplier.getProducts().size();
        switch (supplier.getLevelSupplier()) {
            case Supplier.LEVEL_BASIC:
                return (noProduct < Supplier.NUM_OF_PRODUCTS_FOR_BASIC);
            case Supplier.LEVEL_PREMIUM:
                return (noProduct < Supplier.NUM_OF_PRODUCTS_FOR_PREMIUM);
            case Supplier.LEVEL_MALL:
                return (noProduct < Supplier.NUM_OF_PRODUCTS_FOR_MALL);
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
