package com.ec.recauctionec.controller.supplier;

import com.ec.recauctionec.data.entities.BidJoin;
import com.ec.recauctionec.data.entities.CustomUserDetails;
import com.ec.recauctionec.data.entities.User;
import com.ec.recauctionec.services.BidJoinService;
import com.ec.recauctionec.services.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping(value = "/supplier/phien-dau-gia")
public class SBidController {
    private Authentication auth;
    @Autowired
    private BidService bidService;
    @Autowired
    private BidJoinService joinService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String viewAllAuction(@RequestParam(value = "date-filter", required = false) Date date,
                                 @RequestParam(value = "status", required = false) Boolean status,
                                 ModelMap modelMap) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        User us = ((CustomUserDetails) auth.getPrincipal()).getUser();
        if (date == null)
            date = new Date(new java.util.Date().getTime());
        List<BidJoin> joins = joinService
                .findAllBySupplierAndDate(us.getSuppliers(), date);
        if (status != null) {
            if (status == true)
                joins = joins.stream()
                        .filter(t -> t.getBid().isComplete() == true)
                        .collect(Collectors.toList());
            else
                joins = joins.stream()
                        .filter(t -> t.getBid().isComplete() == false)
                        .collect(Collectors.toList());
        }
        long total = joinService.totalBidJoinOfSupplier(us.getSuppliers());
        long active_size = joinService.totalBidActiveOfSupplier(us.getSuppliers());
        modelMap.addAttribute("total", total);
        modelMap.addAttribute("active_size", active_size);
        modelMap.addAttribute("joins", joins);
        return "supplier/bids";
    }
}
