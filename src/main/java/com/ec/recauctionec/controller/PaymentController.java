package com.ec.recauctionec.controller;

import com.ec.recauctionec.data.entities.User;
import com.ec.recauctionec.data.entities.Wallet;
import com.ec.recauctionec.data.entities.WalletTransaction;
import com.ec.recauctionec.configs.paypal.PaypalPaymentIntent;
import com.ec.recauctionec.configs.paypal.PaypalPaymentMethod;
import com.ec.recauctionec.data.repositories.WalletTransactionRepo;
import com.ec.recauctionec.services.PaypalService;
import com.ec.recauctionec.services.UserService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.Date;

@Controller
public class PaymentController {
    public static final String URL_PAYPAL_SUCCESS = "thanh-toan/thanh-cong";
    public static final String URL_PAYPAL_CANCEL = "thanh-toan/that-bai";
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private PaypalService paypalService;
    @Autowired
    private UserService userService;
    @Autowired
    private WalletTransactionRepo walletTransactionRepo;

    @Value("${server.host}")
    public String CONTEXT_PATH;

    @GetMapping("/thanh-toan")
    public String getPayment(ModelMap modelMap) {
        return "user/load-money";
    }

    @PostMapping("/thanh-toan")
    public String pay(@RequestParam("amount") double amount_value) {
        String cancelUrl = CONTEXT_PATH + "/" + URL_PAYPAL_CANCEL;
        String successUrl = CONTEXT_PATH + "/" + URL_PAYPAL_SUCCESS;
        double amount_to_dollar = amount_value / Wallet.USD_TO_VND;
        try {
            Payment payment = paypalService.createPayment(
                    amount_to_dollar,
                    "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "payment description",
                    cancelUrl,
                    successUrl);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return "redirect:" + links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping(URL_PAYPAL_CANCEL)
    public String cancelPay(ModelMap modelMap) {
        modelMap.addAttribute("title", "Thanh toán thành công");
        modelMap.addAttribute("message", "Thanh toán thành công");
        modelMap.addAttribute("link","tai-khoan/quan-ly-vi");
        modelMap.addAttribute("description", "Cảm ơn bạn đã thực hiện giao dịch");
        return "message";
    }

    @GetMapping(URL_PAYPAL_SUCCESS)
    @Transactional
    public String successPay(@RequestParam("paymentId") String paymentId,
                             @RequestParam("PayerID") String payerId,
                             ModelMap modelMap) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.findByEmail(email);
        Wallet wallet = user.getWallet();
        try {

            Payment payment = paypalService.executePayment(paymentId, payerId);
            double total = Double.parseDouble(
                    payment.getTransactions()
                            .get(0)
                            .getAmount()
                            .getTotal());
            if (payment.getState().equals("approved")) {
                WalletTransaction history = new WalletTransaction();
                history.setWallet(wallet);
                history.setPaymentId(paymentId);
                history.setCreatedDate(new Timestamp(new Date().getTime()));
                history.setValue(total * Wallet.USD_TO_VND);
                //Nap tien
                history.setType(true);
                walletTransactionRepo.save(history);
                double oldBalance = wallet.getAccountBalance();
                wallet.setAccountBalance(oldBalance + history.getValue());
                //
                modelMap.addAttribute("title", "Thanh toán thành công");
                modelMap.addAttribute("message", "Thanh toán thành công");
                modelMap.addAttribute("description", "Cảm ơn bạn đã thực hiện giao dịch");
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "message";
    }
}
