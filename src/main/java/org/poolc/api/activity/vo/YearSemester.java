package org.poolc.api.activity.vo;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class YearSemester {

    private final int year;
    private final int semester;

    public YearSemester(LocalDate localDate) {
        this.year = localDate.getYear();
        this.semester = monthToSemester(localDate);
    }

    public int monthToSemester(LocalDate localDate) {
        return ((localDate.getMonth().getValue() < 2) || (localDate.getMonth().getValue() >= 8)) ? 2 : 1;
    }

    @Override
    public String toString() {
        return year + "-" + semester;
    }
}
