package com.ec.recauctionec;

import com.ec.recauctionec.data.repositories.BidRepos;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RecauctionEcApplicationTests {
    @Autowired
    private BidRepos bidRepos;

    @Test
    void contextLoads() throws Exception {

    }
}
