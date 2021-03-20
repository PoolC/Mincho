package org.poolc.api.member.domain;

import java.util.Collections;
import java.util.List;

public interface Role {
    default boolean isAdmin() {
        return false;
    }

    default boolean isHideInfo() {
        return true;
    }

    default boolean isMember() {
        return false;
    }

    default boolean isSelfToggleable() {
        return false;
    }

    String getDescription();

    default List<MemberRole> getRequiredRoles() {
        return Collections.emptyList();
    }
}
