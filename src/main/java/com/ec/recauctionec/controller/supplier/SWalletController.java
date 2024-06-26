package com.ec.recauctionec.controller.supplier;

import com.ec.recauctionec.data.entities.CustomUserDetails;
import com.ec.recauctionec.data.entities.User;
import com.ec.recauctionec.data.entities.WalletTransaction;
import com.ec.recauctionec.data.repositories.WalletTransactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/supplier/quan-ly-vi")
public class SWalletController {
    private final WalletTransactionRepo walletRepo;
    private Authentication auth;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getWalletDashboard(@RequestParam(required = false) Date dateFilter,
                                     ModelMap modelMap) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        if (dateFilter == null)
            dateFilter = new Date(new java.util.Date().getTime());
        User us = ((CustomUserDetails) auth.getPrincipal()).getUser();

        List<WalletTransaction> logs = walletRepo
                .findLogFromFilterDateToCurrent(dateFilter, us.getWallet().getWalletId());
        modelMap.addAttribute("logs", logs);
        modelMap.addAttribute("wallet", us.getWallet());
        modelMap.addAttribute("filter", dateFilter);
        return "supplier/wallet";
    }
}
