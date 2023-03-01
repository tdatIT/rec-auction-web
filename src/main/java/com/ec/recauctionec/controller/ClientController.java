package com.ec.recauctionec.controller;

import com.ec.recauctionec.dto.UserDTO;
import com.ec.recauctionec.entities.*;
import com.ec.recauctionec.event.OnRegistrationCompleteEvent;
import com.ec.recauctionec.paypal.CheckUser;
import com.ec.recauctionec.service.AuctionService;
import com.ec.recauctionec.service.ProductService;
import com.ec.recauctionec.service.UserService;
import com.ec.recauctionec.variable.Router;
import com.ec.recauctionec.verification.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
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
    private AuctionService auctionService;


    @RequestMapping(value = {"", Router.HOME_PAGE}, method = RequestMethod.GET)
    public String getHomePage(ModelMap modelMap) {
        List<Product> top5trending = productService.findTop5Trending();
        List<AuctionSession> top10Auction = auctionService.findTop10AuctionForDay();
        modelMap.addAttribute("top10Auction", top10Auction);
        modelMap.addAttribute("top5Trending", top5trending);
        return "index";
    }


    //get page sign up
    @RequestMapping(value = Router.REGISTER_PAGE, method = RequestMethod.GET)
    public String getRegister(ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            UserDTO user = new UserDTO();
            modelMap.addAttribute("register", user);
            return "register";
        } else
            return "redirect:/";
    }

    @RequestMapping(value = Router.REGISTER_PAGE, method = RequestMethod.POST)
    public String registerNewAccount(@ModelAttribute UserDTO register, ModelMap modelMap,
                                     HttpServletRequest req, Errors errors) {
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
    public String login(ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "redirect:/";
    }

    @RequestMapping(value = Router.FORGOT_PASS_PAGE, method = RequestMethod.GET)
    public String showForgot(ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "forgot";
        }
        return "redirect:/";
    }

    @RequestMapping(value = Router.RESET_PASS, method = RequestMethod.POST)
    public ResponseEntity resetPassword(@RequestParam String email) {
        User us = userService.findByEmail(email);
        try {
            if (us != null) {
                userService.requestResetPassword(us);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = Router.CONFIRM_RESET, method = RequestMethod.GET)
    public String confirmResetPassword(@RequestParam("token") String token,
                                       ModelMap modelMap) {
        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            return "redirect:/";
        }
        User user = verificationToken.getUser();
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
        try {
            AuctionSession auction = auctionService.findById(auctionId);
            if (auction != null) {
                List<AuctSessJoin> joins = new ArrayList<>(auction.getAuctSessJoins());
                List<AuctionSession> top10Auction = auctionService.findTop10AuctionForDay();
                List<Product> products = new ArrayList<>();
                modelMap.addAttribute("top10Auction", top10Auction);
                Collections.sort(joins, new Comparator<AuctSessJoin>() {
                    @Override
                    public int compare(AuctSessJoin o1, AuctSessJoin o2) {
                        return Double.compare(o1.getPrice(), o2.getPrice());
                    }
                });
                modelMap.addAttribute("auction", auction);
                modelMap.addAttribute("joins", joins);
                modelMap.addAttribute("joined", false);
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
                    modelMap.addAttribute("supp_price", 0);
                } else {
                    User us = ((CustomUserDetails) authentication.getPrincipal()).getUser();
                    products = productService.findProductForAuction(us.getUserId(), auction.
                            getProductTagStr());

                    for (AuctSessJoin j : joins) {
                        if (j.getProduct()
                                .getSupplier()
                                .getUser().getUserId() == us.getUserId()) {
                            modelMap.addAttribute("joined", true);
                            modelMap.addAttribute("supp_price", j.getPrice());
                            break;
                        }
                    }
                }
                modelMap.addAttribute("products", products);
            }
            return "view-auction-detail";
        } catch (Exception e) {
            return "redirect:/thong-bao?type=" + MessageController.NOT_FOUND_AUCTION;
        }
    }

    @GetMapping(value = "/check-email")
    public ResponseEntity checkExistEmail(@RequestParam String email) {
        User us = userService.findByEmail(email);
        if (us != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new CheckUser(200,
                            "Email already register for diff account",
                            false));

        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new CheckUser(200,
                        "Email not exist",
                        true));

    }
}
