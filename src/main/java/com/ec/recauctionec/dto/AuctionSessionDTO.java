package com.ec.recauctionec.dto;

import com.ec.recauctionec.entities.AuctSessJoin;
import com.ec.recauctionec.entities.AuctionSession;
import com.ec.recauctionec.entities.Category;
import com.ec.recauctionec.entities.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

@Data
@EqualsAndHashCode
public class AuctionSessionDTO {
    private int auctionSessId;
    @NotEmpty
    private String productKey;

    @NotEmpty
    private String createDate;

    @NotNull
    private int countDay;

    private String description;

    private String endDate;

    private boolean isComplete;
    @NotNull
    private int categoryId;
    @NotNull
    private double reservePrice;
    @NotEmpty
    private String startDate;

    private MultipartFile[] img;

    @NotNull
    private boolean auto;

    private int userId;

    private String productTagStr;

    private Collection<AuctSessJoin> auctSessJoins;

    private Category categoryByCategoryId;

    private User userByUserId;

    public AuctionSession mapping() throws Exception {
        AuctionSession auction = new AuctionSession();
        //Get date from request
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(startDate);
        //Set current time for properties
        Calendar current = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(date);
        startDate.set(Calendar.HOUR, current.get(Calendar.HOUR));
        startDate.set(Calendar.MINUTE, current.get(Calendar.MINUTE));
        startDate.set(Calendar.SECOND, current.get(Calendar.SECOND));
        //calculate start and end time
        Timestamp startTime = new Timestamp(startDate.getTimeInMillis());
        startDate.add(Calendar.DAY_OF_MONTH, countDay);
        Timestamp endTime = new Timestamp(startDate.getTimeInMillis());
        //Mapping into object
        auction.setProductKey(productKey);
        auction.setReservePrice(reservePrice);
        auction.setCreateDate(new Timestamp(new Date().getTime()));
        auction.setStartDate(startTime);
        auction.setEndDate(endTime);
        auction.setProductTagStr(productTagStr);
        return auction;
    }
}
