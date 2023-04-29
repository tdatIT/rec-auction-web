package com.ec.recauctionec.controller;

import com.ec.recauctionec.data.dto.AuctionSessionDTO;
import com.ec.recauctionec.data.entities.*;
import com.ec.recauctionec.data.variable.Router;
import com.ec.recauctionec.services.*;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/dau-gia")
public class BidController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductTagService productTagService;
    @Autowired
    private BidService bidService;
    @Autowired
    private UserService userService;
    @Autowired
    private BidJoinService joinService;
    @Autowired
    private ProductService productService;
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
            BidJoin bidJoin = new BidJoin();
            bidJoin.setPrice(price);
            bidJoin.setProduct(product);
            bidJoin.setBid(auction);
            bidJoin.setTime(new Timestamp(new java.util.Date().getTime()));
            bidJoin.setStatus(BidJoin.ACTIVE);
            joinService.joinAuction(bidJoin);
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
        List<BidJoin> joins = new ArrayList<>(auction.getBidId());
        for (BidJoin j : joins) {
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