package org.example.service;

import org.example.data.DayAndWorkedHours;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

public class B2bTimerServiceImplTest {


    private B2bTimerServiceImpl subjectUnderTest;

    @BeforeEach
    public void initB2bTimerService() {
        this.subjectUnderTest = new B2bTimerServiceImpl();

    }


    @ParameterizedTest
    @ValueSource(floats = {0f, 5f, 6.5f, 10f, 15f, 23.5f, 24f})
    public void setWorkingHoursForToday_givenStandardHours_thenAreSaved(float workingHours) {
        //when
        subjectUnderTest.setWorkedHoursForToday(workingHours);
        float result = subjectUnderTest.getWorkedHoursForToday();

        //then
        assertEquals(workingHours, result);
    }

    @Test
    public void setWorkingHoursForToday_givenNegativeHours_thenThrowError() {
        //given
        float workingHours = -1;

        //when
        Executable executable = () -> subjectUnderTest.setWorkedHoursForToday(workingHours);

        //then
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    public void setWorkingHoursForToday_givenMoreThan24Hours_thenThrowError() {
        //given
        float workingHours = 25;

        //when
        Executable executable = () -> subjectUnderTest.setWorkedHoursForToday(workingHours);

        //then
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    public void getWorkingHoursForToday_givenWorkingHoursWereNotSetBefore_thenReturn0() {
        //given
        //when
        float result = subjectUnderTest.getWorkedHoursForToday();

        //then
        assertEquals(0f, result);
    }

    @Test
    public void getWorkingHoursForToday_givenWorkingHoursWereSet_thenReturnWorkingHours() {
        //given
        float workingHours = 3f;
        subjectUnderTest.setWorkedHoursForToday(workingHours);

        //when
        float result = subjectUnderTest.getWorkedHoursForToday();

        //then
        assertEquals(workingHours, result);
    }

    @Test
    public void getWorkingHoursForToday_givenWorkingHoursWereSetTheDayBefore_thenReturn0() {
        //given
        LocalDate dateTomorrow = LocalDate.now().plusDays(1);
        subjectUnderTest.setWorkedHoursForToday(4f);

        //when
        float result = invokeWithCustomLocalDateNowValue(dateTomorrow, () -> subjectUnderTest.getWorkedHoursForToday());
        //then
        assertEquals(0f, result);
    }

    @Test
    public void getWorkedHoursForThisMonth_givenStandardDataAndDatesInOtherMonthOrYear() {
        //given
        List<DayAndWorkedHours> validData = List.of(
                new DayAndWorkedHours(LocalDate.of(2023, 7, 10), 5f),
                new DayAndWorkedHours(LocalDate.of(2023, 7, 15), 7.5f),
                new DayAndWorkedHours(LocalDate.of(2023, 7, 16), 0f),
                new DayAndWorkedHours(LocalDate.of(2023, 7, 17), 7.5f),
                new DayAndWorkedHours(LocalDate.of(2023, 7, 24), 9.5f)
        );

        List<DayAndWorkedHours> invalidData = List.of(
                new DayAndWorkedHours(LocalDate.of(2022, 7, 15), 3f),
                new DayAndWorkedHours(LocalDate.of(2023, 6, 15), 2f)
        );

        List<DayAndWorkedHours> data = new ArrayList<>(validData);
        data.addAll(invalidData);
        data.forEach(dayAndWorkedHours ->
                subjectUnderTest.setWorkedHoursFor(dayAndWorkedHours.getDate(), dayAndWorkedHours.getHoursWorked()));

        //when
        Supplier<Float> whenMethod = () ->  subjectUnderTest.getWorkedHoursForThisMonth();

        LocalDate nowStub = LocalDate.of(2023, 7, 26);
         float result = invokeWithCustomLocalDateNowValue(nowStub, whenMethod);

        //then
        assertEquals(29.5f, result);
    }

    @Test
    public void getWorkedHoursForThisMonth_givenNoData(){
        //given
        //when
        float result = subjectUnderTest.getWorkedDaysThisMonth();

        //then
        assertEquals(0f, result);
    }

    @Test
    public void setWorkedHoursFor_givenTwiceTheSameDate(){
        //given
        LocalDate date = LocalDate.of(2023, 7, 24);
        subjectUnderTest.setWorkedHoursFor(date, 5f);
        subjectUnderTest.setWorkedHoursFor(date, 7f);

        //when
        float result = invokeWithCustomLocalDateNowValue(date, () -> subjectUnderTest.getWorkedHoursForThisMonth());

        //then
        assertEquals(7f, result);
    }

    @Test
    public void getWorkedDaysThisMonth_givenStandardDataAndDataInOtherMonthsAndOtherYears(){
        //given
        LocalDate currentStubDate = LocalDate.of(2023, 7, 24);

        List<LocalDate> validDates = List.of(
                LocalDate.of(2023, 7, 1),
                LocalDate.of(2023, 7, 10),
                LocalDate.of(2023, 7, 17),
                LocalDate.of(2023, 7, 29)
        );

        List<LocalDate> invalidDates = List.of(
                LocalDate.of(2024, 7, 5),
                LocalDate.of(2022, 7, 5),
                LocalDate.of(2023, 6, 5),
                LocalDate.of(2023, 8, 5)
        );

        List<LocalDate> dates = new ArrayList<>(validDates);
        dates.addAll(invalidDates);
        dates.forEach(date -> subjectUnderTest.setWorkedHoursFor(date, 4f));

        //when
        int result = invokeWithCustomLocalDateNowValue(currentStubDate, () -> subjectUnderTest.getWorkedDaysThisMonth());

        //then
        assertEquals(4, result);
    }

    public <T> T invokeWithCustomLocalDateNowValue(LocalDate date, Supplier<T> supplier){
        try (MockedStatic<LocalDate> mockedStatic = mockStatic(LocalDate.class)) {
            mockedStatic.when(LocalDate::now).thenReturn(date);
            return supplier.get();
        }
    }

}
