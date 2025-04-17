package org.kuraterut.zoohm2hse.infrastructure.converters;

import org.kuraterut.zoohm2hse.domain.valueobjects.feedingSchedule.FeedingTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import java.time.LocalTime;

@Component
public class LocalTimeToFeedingTimeConverter implements Converter<LocalTime, FeedingTime> {
    @Override
    public FeedingTime convert(LocalTime source) {
        return new FeedingTime(source);
    }
}