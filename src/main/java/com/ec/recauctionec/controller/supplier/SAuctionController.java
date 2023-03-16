package com.ec.recauctionec.controller.supplier;

import com.ec.recauctionec.entities.AuctSessJoin;
import com.ec.recauctionec.entities.CustomUserDetails;
import com.ec.recauctionec.entities.User;
import com.ec.recauctionec.services.AuctSessJoinService;
import com.ec.recauctionec.services.AuctionService;
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
public class SAuctionController {
    private Authentication auth;
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private AuctSessJoinService joinService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String viewAllAuction(@RequestParam(value = "date-filter", required = false) Date date,
                                 @RequestParam(value = "status", required = false) Boolean status,
                                 ModelMap modelMap) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        User us = ((CustomUserDetails) auth.getPrincipal()).getUser();
        if (date == null)
            date = new Date(new java.util.Date().getTime());
        List<AuctSessJoin> joins = joinService
                .findAllBySupplierAndDate(us.getSuppliers(), date);
        if (status != null) {
            if (status == true)
                joins = joins.stream()
                        .filter(t -> t.getAuctionSession().isComplete() == true)
                        .collect(Collectors.toList());
            else
                joins = joins.stream()
                        .filter(t -> t.getAuctionSession().isComplete() == false)
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
