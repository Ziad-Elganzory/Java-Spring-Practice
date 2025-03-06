package com.codezury.springcoredemo.config;

import com.codezury.springcoredemo.common.Coach;
import com.codezury.springcoredemo.common.SwimCoach;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SportConfig {

    @Bean("aquatic") // you can add custom bean id to the annotation as an argument
    public Coach swimCoach(){
        return new SwimCoach();
    }
}
