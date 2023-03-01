package com.ec.recauctionec.variable;

public class RoleConst {
    public static final int ADMIN = 1;
    public static final int SUPPLIER = 2;
    public static final int USER = 3;
    public static final int GUEST = 4;

    public static String getNameRole(int role_id) {
        switch (role_id) {
            case 1:
                return "ROLE_ADMIN";
            case 2:
                return "ROLE_SUPPLIER";
            case 3:
                return "ROLE_USER";
            case 4:
                return "ROLE_GUEST";
        }
        return "NO";
    }
}
