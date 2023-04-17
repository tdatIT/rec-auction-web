package com.ec.recauctionec.controller;

import com.ec.recauctionec.configs.paypal.CheckUser;
import com.ec.recauctionec.data.dto.UserDTO;
import com.ec.recauctionec.data.entities.*;
import com.ec.recauctionec.data.variable.Router;
import com.ec.recauctionec.event.OnRegistrationCompleteEvent;
import com.ec.recauctionec.services.BidService;
import com.ec.recauctionec.services.ProductService;
import com.ec.recauctionec.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class ClientController {
    @Autowired
    private UserService userService;

    @Autowired
    ProductService productService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private BidService bidService;

    private Authentication auth;


    @RequestMapping(value = {"", Router.HOME_PAGE}, method = RequestMethod.GET)
    public String getHomePage(ModelMap modelMap) {
        List<Product> top5trending = productService.findTop5Trending();
        List<Bid> top10Auction = bidService.findTop10AuctionForDay();
        modelMap.addAttribute("top10Auction", top10Auction);
        modelMap.addAttribute("top5Trending", top5trending);
        return "index";
    }


    //get page sign up
    @RequestMapping(value = Router.REGISTER_PAGE, method = RequestMethod.GET)
    public String getRegister(ModelMap modelMap) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            UserDTO user = new UserDTO();
            modelMap.addAttribute("register", user);
            return "register";
        } else
            return "redirect:/";
    }

    @RequestMapping(value = Router.REGISTER_PAGE, method = RequestMethod.POST)
    public String registerNewAccount(@ModelAttribute UserDTO register,
                                     HttpServletRequest req) {
        try {
            User notActiveAcc = userService.registerAccount(register.mappingClass());
            String appUrl = req.getContextPath();
            applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(notActiveAcc,
                    req.getLocale(), appUrl));
        } catch (RuntimeException ex) {
            return "redirect:" + Router.REGISTER_PAGE + "?error=true";
        }
        return "redirect:" + Router.MESSAGE + "?type=" + MessageController.VERIFY_TOKEN;
    }

    @RequestMapping(value = Router.LOGIN_PAGE, method = RequestMethod.GET)
    public String login() {
        auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "redirect:/";
    }

    @RequestMapping(value = Router.FORGOT_PASS_PAGE, method = RequestMethod.GET)
    public String showForgot() {
        auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            return "forgot";
        }
        return "redirect:/";
    }

    @RequestMapping(value = Router.RESET_PASS, method = RequestMethod.POST)
    public String resetPassword(@RequestParam String email) {
        User us = userService.findByEmail(email);
        try {
            if (us != null) {
                userService.requestResetPassword(us);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:" + Router.MESSAGE + "?type=" + MessageController.VERIFY_TOKEN;
    }

    @RequestMapping(value = Router.CONFIRM_RESET, method = RequestMethod.GET)
    public String confirmResetPassword(@RequestParam String token,
                                       ModelMap modelMap) {
        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            return "redirect:/";
        }
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return "redirect:/";
        }
        modelMap.addAttribute("token", token);
        return "reset-password";
    }

    @RequestMapping(value = Router.SET_PASS, method = RequestMethod.POST)
    public String setNewPassword(@RequestParam("token") String token,
                                 @RequestParam("password") String password) {
        userService.resetPassword(token, password);
        return "redirect:/dang-nhap";
    }

    @RequestMapping(value = "/chi-tiet-dau-gia/{id}", method = RequestMethod.GET)
    public String viewAuctionDetails(@PathVariable("id") int auctionId, ModelMap modelMap) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        Bid auction = bidService.findById(auctionId);
        if (auction != null) {
            List<BidJoin> joins = new ArrayList<>(auction.getBidJoins());
            List<Bid> top10Auction = bidService.findTop10AuctionForDay();
            List<Product> products = new ArrayList<>();
            modelMap.addAttribute("top10Auction", top10Auction);
            Collections.sort(joins, new Comparator<BidJoin>() {
                @Override
                public int compare(BidJoin o1, BidJoin o2) {
                    return Double.compare(o1.getPrice(), o2.getPrice());
                }
            });
            modelMap.addAttribute("auction", auction);
            modelMap.addAttribute("joins", joins);
            modelMap.addAttribute("joined", 1);
            //Check anonymous view
            if (auth == null || auth instanceof AnonymousAuthenticationToken) {
                modelMap.addAttribute("supp_price", 0);
            } else {
                //Does the product belong to the user?
                User us = ((CustomUserDetails) auth.getPrincipal()).getUser();
                if (us.getUserId() == auction.getUser().getUserId()) {
                    modelMap.addAttribute("joined", 0);
                } else {
                    //Did user join auction last time?
                    BidJoin last_bid = joins.stream()
                            .filter(bid -> bid.getProduct()
                                    .getSupplier()
                                    .getUser().getUserId() == us.getUserId())
                            .findFirst().orElse(null);
                    //if joined show btn with text set new bid
                    //else show btn with text choose the product for join auction
                    if (last_bid != null) {
                        modelMap.addAttribute("joined", 2);
                        modelMap.addAttribute("supp_price", last_bid.getPrice());
                    } else {
                        products = productService.findProductForAuction(
                                us.getUserId(),
                                auction.getProductTagStr());

                    }
                }
            }
            modelMap.addAttribute("products", products);
            return "view-auction-detail";
        }
        return "redirect:/thong-bao?type=" + MessageController.NOT_FOUND_AUCTION;
    }


}
