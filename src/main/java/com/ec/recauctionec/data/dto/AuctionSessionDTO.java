package com.ec.recauctionec.data.dto;

import com.ec.recauctionec.data.entities.BidParticipant;
import com.ec.recauctionec.data.entities.Category;
import com.ec.recauctionec.data.entities.Bid;
import com.ec.recauctionec.data.entities.User;

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
    private int bidSessId;
    @NotEmpty
    private String productKey;

    @NotEmpty
    private String createdDate;

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

    private Collection<BidParticipant> bidderData;

    private User user;

    public Bid mapping() throws Exception {
        Bid bid = new Bid();
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
        bid.setCreatedDate(new Timestamp(new Date().getTime()));
        bid.setStartDate(startTime);
        bid.setEndedDate(endTime);
        //Mapping into object
        bid.setProductKey(productKey);
        bid.setReservePrice(reservePrice);
        bid.setDescription(description);
        bid.setProductTagStr(productTagStr);
        bid.setCategory(category);
        return bid;
    }
}
