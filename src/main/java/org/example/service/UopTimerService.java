package org.example.service;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

public interface UopTimerService {
    float setAsNonWorkingDay(LocalDate date);

    float getWorkingDaysInMonth(Year year, Month month);

}
