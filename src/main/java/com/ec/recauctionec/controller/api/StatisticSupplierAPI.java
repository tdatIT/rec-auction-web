package com.ec.recauctionec.controller.api;

import com.ec.recauctionec.data.entities.CustomUserDetails;
import com.ec.recauctionec.data.entities.User;
import com.ec.recauctionec.data.repositories.WalletHistoryRepo;
import com.ec.recauctionec.data.response.WalletObjQuery;
import com.ec.recauctionec.data.response.WalletStatisticResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/supplier/statistic-wallet")
public class StatisticSupplierAPI {
    @Autowired
    WalletHistoryRepo historyRepo;
    private Authentication auth;

    @RequestMapping(value = "/line-chart", method = RequestMethod.GET)
    public ResponseEntity<WalletStatisticResp> getStatisticWalletForLine(@RequestParam Date filterDate) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        User us = ((CustomUserDetails) auth.getPrincipal()).getUser();
        List<WalletObjQuery> data = historyRepo
                .statisticTransactionForLineChart(us.getWallet().getWalletId(), filterDate);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new WalletStatisticResp(
                        200, "get data to success", data
                ));
    }

    @RequestMapping(value = "/pie-chart", method = RequestMethod.GET)
    public ResponseEntity<WalletStatisticResp> getStatisticWalletForPie(@RequestParam Date filterDate) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        User us = ((CustomUserDetails) auth.getPrincipal()).getUser();
        List<WalletObjQuery> data = historyRepo
                .statisticTransactionForPieChart(us.getWallet().getWalletId(), filterDate);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new WalletStatisticResp(
                        200, "get data to success", data
                ));
    }

}
