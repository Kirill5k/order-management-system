package io.kirill.notificationservice.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paranamer.ParanamerModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

  @Bean
  ObjectMapper objectMapper() {
    var objectMapper = new ObjectMapper();
    objectMapper.registerModule(new ParanamerModule());
    return objectMapper;
  }
}
