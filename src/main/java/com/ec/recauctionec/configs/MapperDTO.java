package com.ec.recauctionec.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MapperDTO {
    @Bean
    public ModelMapper injectModelMapper() {
        return new ModelMapper();
    }
}
