package org.poolc.api.common.domain;

import lombok.Getter;

import java.time.LocalDate;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Getter
public class YearSemester {

    private final int year;
    private final int semester;

    public YearSemester(int year, int semester) {
        this.year = year;
        this.semester = semester;
    }

    public static YearSemester of(LocalDate localDate) {
        int year;
        int semester;
        if (localDate.getMonth().getValue() <= 2) {
            year = localDate.getYear() - 1;
            semester = 2;
        } else if (localDate.getMonth().getValue() < 9) {
            year = localDate.getYear();
            semester = 1;
        } else {
            year = localDate.getYear();
            semester = 2;
        }
        return new YearSemester(year, semester);
    }

    public static YearSemester getYearSemesterFromString(String value) {
        String[] splits = value.split("-");
        if (splits.length != 2) {
            throw new IllegalArgumentException("제대로된 형식의 시간이 아닙니다");
        }
        Integer year = tryParse(splits[0]);
        Integer semester = tryParse(splits[1]);
        return new YearSemester(year, semester);
    }

    public LocalDate getFirstDateFromYearSemester() {
        if (semester == 1) {
            return LocalDate.of(year, 3, 1);
        } else {
            return LocalDate.of(year, 9, 1);
        }
    }

    public LocalDate getLastDateFromYearSemester() {
        if (semester == 1) {
            return LocalDate.of(year, 8, 1).with(lastDayOfMonth());
        } else {
            return LocalDate.of(year + 1, 2, 1).with(lastDayOfMonth());
        }
    }

    private static Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("제대로된 형식의 시간이 아닙니다");
        }
    }

    @Override
    public String toString() {
        return year + "-" + semester;
    }
}
