package sk.durovic.services;

import java.time.LocalDateTime;

public interface AvailabilityService {

    boolean isAvailable(LocalDateTime start, LocalDateTime end);
}
