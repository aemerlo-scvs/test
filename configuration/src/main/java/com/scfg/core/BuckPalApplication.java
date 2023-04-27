package com.scfg.core;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication
@EnableEncryptableProperties
@EnableJpaAuditing
@EnableScheduling
public class BuckPalApplication {

    private static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("America/La_Paz");
    private static final Locale DEFAULT_LOCALE = new Locale("es", "ES");

    public static void main(String[] args) {
        init();
        SpringApplication.run(BuckPalApplication.class, args);

    }

    private static void init() {
        TimeZone.setDefault(DEFAULT_TIME_ZONE);
        Locale.setDefault(DEFAULT_LOCALE);
    }

    @Component
    class MyRunner implements CommandLineRunner {

        @Autowired
        private Environment environment;

        @Override
        public void run(String... args) throws Exception {

            if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
                System.out.println("Development configuration active");
            }

            if (Arrays.asList(environment.getActiveProfiles()).contains("pre-prod")) {
                System.out.println("Pre-Production configuration active");
            }

            if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
                System.out.println("Production configuration active");
            }

            System.out.println("Active profiles: " +
                    Arrays.toString(environment.getActiveProfiles()));
        }
    }

}
