package com.ec.recauctionec.controller;

import com.ec.recauctionec.data.dto.AuctionSessionDTO;
import com.ec.recauctionec.data.entities.*;
import com.ec.recauctionec.data.variable.Router;
import com.ec.recauctionec.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/dau-gia")
public class BidController {
    
    private final CategoryService categoryService;
    private final ProductTagService productTagService;
    private final BidService bidService;
    private final UserService userService;
    private final BidParticipantService joinService;
    private final ProductService productService;
    private Authentication auth;


    @RequestMapping(value = "/tao-phien", method = RequestMethod.GET)
    public String getCreateAuction(ModelMap modelMap) {
        List<Category> categories = categoryService.findAll();
        List<ProductTag> tags = productTagService.findAll();
        AuctionSessionDTO dto = new AuctionSessionDTO();
        modelMap.addAttribute("dto", dto);
        modelMap.addAttribute("tags", tags);
        modelMap.addAttribute("categories", categories);
        return "user/create-auction";
    }

    @PostMapping(value = "/tao-phien")
    public String createAuction(@ModelAttribute AuctionSessionDTO dto) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        User us = ((CustomUserDetails) auth.getPrincipal()).getUser();
        if (bidService.createNewAuction(us, dto)) {
            return "redirect:" + Router.MESSAGE + "?type=" + MessageController.CREATE_SUCCESS;
        }
        return "redirect:" + Router.MESSAGE + "?type=" + MessageController.CREATE_FAIL;

    }

    @PostMapping(value = "/tham-gia")
    public String joinAuction(@RequestParam("auctionId") int auctionId,
                              @RequestParam("productId") int productId,
                              @RequestParam("price") double price) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        Bid auction = bidService.findById(auctionId);
        Product product = productService.findById(productId);
        if (product.getSupplier()
                .getUser().getUserId() != auction.getUser().getUserId()) {
            BidParticipant bidParticipant = new BidParticipant();
            bidParticipant.setPrice(price);
            bidParticipant.setProduct(product);
            bidParticipant.setBid(auction);
            bidParticipant.setTime(new Timestamp(new java.util.Date().getTime()));
            bidParticipant.setStatus(BidParticipant.ACTIVE);
            joinService.joinAuction(bidParticipant);
        }
        return "redirect:/chi-tiet-dau-gia/" + auctionId;
    }

    @GetMapping(value = {"/quan-ly-phien", ""})
    public String getAllAuctionOfUser(@RequestParam(value = "date-filter", required = false) Date date,
                                      ModelMap modelMap) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        User us = ((CustomUserDetails) auth.getPrincipal()).getUser();
        List<Bid> data;
        if (date != null)
            data = bidService.findAllByUserAndActive(us.getUserId(), date);
        else
            data = bidService.find5LastBidByUserId(us);
        modelMap.addAttribute("data", data);
        return "/user/all-auction";
    }

    @PostMapping(value = "/dat-gia-moi")
    public ResponseEntity getJoinAuction(@RequestParam("auctionId") int auctionId,
                                         @RequestParam("price") double price) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        User us = ((CustomUserDetails) auth.getPrincipal()).getUser();
        Bid auction = bidService.findById(auctionId);
        List<BidParticipant> joins = new ArrayList<>(auction.getBidId());
        for (BidParticipant j : joins) {
            if (j.getProduct()
                    .getSupplier()
                    .getUser().getUserId() == us.getUserId()) {
                j.setPrice(price);
                joinService.updateJoin(j);
                return new ResponseEntity(HttpStatus.OK);
            }
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}