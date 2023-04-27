package com.scfg.core.common.util;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

//    public BCryptPasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder(10);
//    }


//    public BCryptPasswordEncoder PasswordEncoder() {
//        return new BCryptPasswordEncoder(10);
//    }

    @Bean
    public BCryptPasswordEncoder password(){
        return new BCryptPasswordEncoder(10);
    }

}
