package com.ec.recauctionec.data.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletStatisticResp {
    private int code;
    private String message;
    private List<WalletObjQuery> data;
}
