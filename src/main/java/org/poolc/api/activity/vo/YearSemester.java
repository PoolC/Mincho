package org.poolc.api.activity.vo;

import lombok.Getter;

import java.time.LocalDate;

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

    @Override
    public String toString() {
        return year + "-" + semester;
    }
}
