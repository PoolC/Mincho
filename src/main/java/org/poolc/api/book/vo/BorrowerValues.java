package org.poolc.api.book.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.member.domain.Member;

@Getter
public class BorrowerValues {
    private final String id;
    private final String name;

    @JsonCreator
    public BorrowerValues(Member member) {
        this.id = member.getUUID();
        this.name = member.getName();
    }
}