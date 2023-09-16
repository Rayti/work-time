package org.example.data;


import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DayAndWorkedHoursTest {

    
    @Test
    public void dayAndWorkingHoursConstructor(){
        new DayAndWorkedHours(LocalDate.now(), 10);
    }

    @Test
    public void getters(){
        //given
        LocalDate date = LocalDate.now();
        float hoursWorked = 5f;
        DayAndWorkedHours subjectUnderTest = new DayAndWorkedHours(date, hoursWorked);

        //when
        LocalDate resultDate = subjectUnderTest.getDate();
        float resultHoursWorked = subjectUnderTest.getHoursWorked();

        //then
        assertEquals(date, resultDate);
        assertEquals(hoursWorked, resultHoursWorked);
    }

    @Test
    public void setters(){
        //given
        LocalDate date = LocalDate.now();
        float hoursWorked = 5f;
        DayAndWorkedHours subjectUnderTest = new DayAndWorkedHours(null, 0f);

        //when
        subjectUnderTest.setDate(date);
        subjectUnderTest.setHoursWorked(hoursWorked);

        LocalDate resultDate = subjectUnderTest.getDate();
        float resultHoursWorked = subjectUnderTest.getHoursWorked();

        //then
        assertEquals(date, resultDate);
        assertEquals(hoursWorked, resultHoursWorked);
    }

    @Test
    public void isMonthAndYear(){
        //given
        LocalDate date = LocalDate.of(2023, 7, 24);
        float hoursWorked = 5f;
        DayAndWorkedHours subjectUnderTest = new DayAndWorkedHours(date, hoursWorked);

        //when
        boolean resultOk1 = subjectUnderTest.isMonthAndYear(date);
        boolean resultOk2 = subjectUnderTest.isMonthAndYear(date.plusDays(1));
        boolean resultOk3 = subjectUnderTest.isMonthAndYear(date.minusDays(2));

        boolean resultBad1 = subjectUnderTest.isMonthAndYear(LocalDate.of(2023, 8, 24));
        boolean resultBad2 = subjectUnderTest.isMonthAndYear(LocalDate.of(2024, 7, 24));
        boolean resultBad3 = subjectUnderTest.isMonthAndYear(LocalDate.of(2024, 9, 24));

        //then
        assertTrue(resultOk1);
        assertTrue(resultOk2);
        assertTrue(resultOk3);
        assertFalse(resultBad1);
        assertFalse(resultBad2);
        assertFalse(resultBad3);
    }
}
