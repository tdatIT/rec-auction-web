package com.ec.recauctionec;

import com.ec.recauctionec.repositories.AuctionRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RecauctionEcApplicationTests {
    @Autowired
    private AuctionRepo auctionRepo;

    @Test
    void contextLoads() throws Exception {

    }
}
