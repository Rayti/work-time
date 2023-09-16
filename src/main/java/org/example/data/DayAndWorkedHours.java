package org.example.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DayAndWorkedHours {
    private LocalDate date;
    private float hoursWorked;

    public boolean isMonthAndYear(LocalDate dateToCompare) {
        return date.getYear() == dateToCompare.getYear()
                && date.getMonth().equals(dateToCompare.getMonth());
    }
}

