package org.example.service;

import org.example.data.DayAndWorkedHours;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class B2bTimerServiceImpl implements B2bTimerService{

    private final List<DayAndWorkedHours> cache = new ArrayList<>();

    @Override
    public void setWorkedHoursForToday(float workedHours) {
        setWorkedHoursFor(LocalDate.now(), workedHours);
    }

    @Override
    public void setWorkedHoursFor(LocalDate date, float workedHours) {
        if (workedHours < 0) {
            throw new IllegalArgumentException("Working hours can't be negative.");
        }

        if(workedHours > 24) {
            throw new IllegalArgumentException("There are more working hours than all hours available in the day.");
        }

        Consumer<DayAndWorkedHours> workedHoursSetter = dayAndWorkedHours -> dayAndWorkedHours.setHoursWorked(workedHours);
        Runnable newDayAndWorkedHoursCacheAdder = () -> cache.add(new DayAndWorkedHours(date, workedHours));

        cache.stream()
                .filter(dayAndWorkedHours -> dayAndWorkedHours.getDate().equals(date))
                .findFirst()
                .ifPresentOrElse(workedHoursSetter, newDayAndWorkedHoursCacheAdder);
    }

    @Override
    public float getWorkedHoursForToday(){
        return cache.stream()
                .filter(dayAndWorkedHours -> dayAndWorkedHours.getDate().equals(LocalDate.now()))
                .findFirst()
                .map(DayAndWorkedHours::getHoursWorked)
                .orElse(0f);
    }

    @Override
    public float getWorkedHoursForThisMonth() {
        return (float) cache.stream()
                .filter(dayAndWorkedHours -> dayAndWorkedHours.isMonthAndYear(LocalDate.now()))
                .mapToDouble(DayAndWorkedHours::getHoursWorked)
                .sum();
    }

    @Override
    public int getWorkedDaysThisMonth() {
        return (int) cache.stream()
                .filter(dayAndWorkedHours -> dayAndWorkedHours.isMonthAndYear(LocalDate.now()))
                .count();
    }


}
