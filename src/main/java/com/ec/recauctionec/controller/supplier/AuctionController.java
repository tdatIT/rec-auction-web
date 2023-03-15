package com.ec.recauctionec.controller.supplier;

import com.ec.recauctionec.entities.AuctSessJoin;
import com.ec.recauctionec.entities.AuctionSession;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping(name = "/supplier/phien-dau-gia")
public class AuctionController {
    private Authentication auth;
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private AuctSessJoinService joinService;

    @RequestMapping(name = "", method = RequestMethod.GET)
    public String viewAllAuction(@RequestParam(value = "date-filter", required = false)
                                 Date date, ModelMap modelMap) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        User us = ((CustomUserDetails) auth).getUser();
        List<AuctSessJoin> joins = joinService
                .findAllBySupplierAndDate(us.getSuppliers(), date);
        List<AuctionSession> auctions = new ArrayList<>();
        if (joins != null) {
            auctions = joins
                    .stream()
                    .map(t -> t.getAuctionSession())
                    .collect(Collectors.toList());

        }
        modelMap.addAttribute("auction", auctions);
        return "supplier/bids";
    }
}
