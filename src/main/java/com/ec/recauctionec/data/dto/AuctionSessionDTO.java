package com.ec.recauctionec.data.dto;

import com.ec.recauctionec.data.entities.AuctionSession;
import com.ec.recauctionec.data.entities.User;
import com.ec.recauctionec.data.entities.AuctSessJoin;
import com.ec.recauctionec.data.entities.Category;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
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
    private Category category;
    @NotNull
    private double reservePrice;
    @NotEmpty
    private Date startDate;

    private MultipartFile[] img;

    @NotNull
    private boolean auto;

    private int userId;

    private String productTagStr;

    private Collection<AuctSessJoin> auctSessJoins;

    private User user;

    public AuctionSession mapping() throws Exception {
        AuctionSession auction = new AuctionSession();
        //Set current time for properties
        Calendar current = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(startDate.getTime());
        startDate.set(Calendar.HOUR, current.get(Calendar.HOUR));
        startDate.set(Calendar.MINUTE, current.get(Calendar.MINUTE));
        startDate.set(Calendar.SECOND, current.get(Calendar.SECOND));
        //calculate start and end time
        Timestamp startTime = new Timestamp(startDate.getTimeInMillis());
        startDate.add(Calendar.DAY_OF_MONTH, countDay);
        Timestamp endTime = new Timestamp(startDate.getTimeInMillis());
        //set create time
        auction.setCreateDate(new Timestamp(new Date().getTime()));
        auction.setStartDate(startTime);
        auction.setEndDate(endTime);
        //Mapping into object
        auction.setProductKey(productKey);
        auction.setReservePrice(reservePrice);
        auction.setDescription(description);
        auction.setProductTagStr(productTagStr);
        auction.setCategory(category);
        return auction;
    }
}
