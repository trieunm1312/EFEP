package com.team1.efep;

import com.team1.efep.enums.Const;
import com.team1.efep.models.entity_models.*;
import com.team1.efep.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class EfepApplication {

    public static void main(String[] args) {
        SpringApplication.run(EfepApplication.class, args);
    }


}
