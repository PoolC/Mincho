package org.poolc.api.activity.vo;

import lombok.Getter;

@Getter
public class YearSemester {

    private final Long year;
    private final Long semester;

    public YearSemester(Long year, Long semester) {
        this.year = year;
        this.semester = semester;
    }

    @Override
    public String toString() {
        return year + "-" + semester;
    }
}
