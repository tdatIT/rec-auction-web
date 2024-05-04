package com.ec.recauctionec;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RecauctionEcApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecauctionEcApplication.class, args);
    }

}
