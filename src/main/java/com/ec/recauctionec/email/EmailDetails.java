package com.ec.recauctionec.email;

import lombok.*;

@Data
@AllArgsConstructor
public class EmailDetails {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;

    public EmailDetails() {
    }

}
