package com.ec.recauctionec.controller;

import com.ec.recauctionec.configs.UrlUtils;
import com.ec.recauctionec.entities.User;
import com.ec.recauctionec.entities.Wallet;
import com.ec.recauctionec.entities.WalletHistory;
import com.ec.recauctionec.paypal.PaypalPaymentIntent;
import com.ec.recauctionec.paypal.PaypalPaymentMethod;
import com.ec.recauctionec.repositories.WalletHistoryRepo;
import com.ec.recauctionec.repositories.WalletRepo;
import com.ec.recauctionec.service.PaypalService;
import com.ec.recauctionec.service.UserService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
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
    private WalletHistoryRepo walletHistoryRepo;
    @Autowired
    private WalletRepo walletRepo;

    @GetMapping("/thanh-toan")
    public String getPayment(ModelMap modelMap) {
        return "user/load-money";
    }

    @PostMapping("/thanh-toan")
    public String pay(HttpServletRequest request, @RequestParam("amount") double amount_value) {
        String cancelUrl = UrlUtils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
        String successUrl = UrlUtils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
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
                WalletHistory history = new WalletHistory();
                history.setWallet(wallet);
                history.setPaymentId(paymentId);
                history.setCreateDate(new Timestamp(new Date().getTime()));
                history.setValue(total * Wallet.USD_TO_VND);
                //Nap tien
                history.setType(true);
                walletHistoryRepo.save(history);
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
