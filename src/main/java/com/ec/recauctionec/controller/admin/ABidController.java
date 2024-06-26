package com.ec.recauctionec.controller.admin;

import com.ec.recauctionec.data.entities.Bid;
import com.ec.recauctionec.services.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/dau-gia")
public class ABidController {

    private final BidService bidService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getAuctionList(@RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer size,
                                 @RequestParam(name = "date-filter",required = false) Date filter_date,
                                 ModelMap modelMap) {
        if (page == null)
            page = 0;
        if (size == null)
            size = 20;
        if (filter_date == null)
            filter_date = new Date(new java.util.Date().getTime());
        List<Bid> bids =
                bidService.findAllByDateAndPageSize(page, size, filter_date);
        long total_end = bids.stream().filter(t -> t.isComplete() == true).count();
        modelMap.addAttribute("date", filter_date);
        modelMap.addAttribute("bids", bids);
        modelMap.addAttribute("total_end",total_end);
        return "admin/bids";
    }

}
