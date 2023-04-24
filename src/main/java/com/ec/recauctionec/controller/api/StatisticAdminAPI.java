package com.ec.recauctionec.controller.api;

import com.ec.recauctionec.data.repositories.CommissionRepo;
import com.ec.recauctionec.data.repositories.WalletHistoryRepo;
import com.ec.recauctionec.data.response.BestSellerQuery;
import com.ec.recauctionec.data.response.CommissionType;
import com.ec.recauctionec.data.response.OrderTypeQuery;
import com.ec.recauctionec.data.response.WalletObjQuery;
import com.ec.recauctionec.services.OrderService;
import com.ec.recauctionec.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class StatisticAdminAPI {
    @Autowired
    private WalletHistoryRepo walletHistoryRepo;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CommissionRepo commissionRepo;
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/totalTransactionInMonth/{month}/{year}", method = RequestMethod.GET)
    public ResponseEntity getTotalTransactionInMonth(@PathVariable(required = false) Integer month, @PathVariable(required = false) Integer year) {
        if (year == null || month == null) {
            LocalDate current = LocalDate.now();
            year = current.getYear();
            month = current.getMonthValue();
        }
        List<WalletObjQuery> data = walletHistoryRepo.statisticAllTransactionInMonth(month, year);
        if (data == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data is empty");
        }
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @RequestMapping(value = "/totalOrderInMonth/{month}/{year}", method = RequestMethod.GET)
    public ResponseEntity getTotalOrderInMonth(@PathVariable(required = false) Integer month,
                                               @PathVariable(required = false) Integer year) {
        if (year == null || month == null) {
            LocalDate current = LocalDate.now();
            year = current.getYear();
            month = current.getMonthValue();
        }
        List<OrderTypeQuery> data = orderService.getTotalOrderInMonth(month, year);
        if (data == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data is empty");
        }
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @RequestMapping(value = "/topBestSellerInMonth/{month}/{year}", method = RequestMethod.GET)
    public ResponseEntity getBestSellProductInMonth(@PathVariable(required = false) Integer month,
                                                    @PathVariable(required = false) Integer year) {
        if (year == null || month == null) {
            LocalDate current = LocalDate.now();
            year = current.getYear();
            month = current.getMonthValue();
        }
        List<BestSellerQuery> data = productService.getTopSeller(month, year);
        if (data == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data is empty");
        }
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @RequestMapping(value = "/totalCommission/{month}/{year}", method = RequestMethod.GET)
    public ResponseEntity getTotalCommission(@PathVariable(required = false) Integer month,
                                             @PathVariable(required = false) Integer year) {
        if (year == null || month == null) {
            LocalDate current = LocalDate.now();
            year = current.getYear();
            month = current.getMonthValue();
        }
        List<CommissionType> data = commissionRepo.getTotalCommissionInMonth(month, year);
        if (data == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data is empty");
        }
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }
}
