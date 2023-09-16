package org.example.service;

import java.time.LocalDate;

public interface B2bTimerService {
    void setWorkedHoursForToday(float workedHours);

    void setWorkedHoursFor(LocalDate date, float workedHours);

    float getWorkedHoursForToday();

    float getWorkedHoursForThisMonth();

    int getWorkedDaysThisMonth();

}
