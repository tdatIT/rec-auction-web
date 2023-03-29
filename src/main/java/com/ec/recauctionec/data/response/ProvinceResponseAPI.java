package com.ec.recauctionec.data.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProvinceResponseAPI {
    private String name;
    private String code;
}
