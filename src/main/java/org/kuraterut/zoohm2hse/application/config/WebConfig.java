package org.kuraterut.zoohm2hse.application.config;

import org.kuraterut.zoohm2hse.infrastructure.converters.LocalTimeToFeedingTimeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new LocalTimeToFeedingTimeConverter());
    }
}