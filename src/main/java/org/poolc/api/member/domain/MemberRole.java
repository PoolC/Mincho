package org.poolc.api.member.domain;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public enum MemberRole implements Role {
    // enum 순서 중요함. 위로 갈수록 높은 권한이라고 가정

    SUPER_ADMIN {
        @Override
        public boolean isAdmin() {
            return true;
        }

        @Override
        public boolean isMember() {
            return true;
        }

        @Override
        public String getDescription() {
            return "슈퍼 관리자";
        }

        @Override
        public List<MemberRole> getRequiredRoles() {
            return Collections.singletonList(MemberRole.MEMBER);
        }
    },
    ADMIN {
        @Override
        public boolean isAdmin() {
            return true;
        }

        @Override
        public boolean isHideInfo() {
            return false;
        }

        @Override
        public boolean isMember() {
            return true;
        }

        @Override
        public String getDescription() {
            return "임원진";
        }

        @Override
        public List<MemberRole> getRequiredRoles() {
            return Collections.singletonList(MemberRole.MEMBER);
        }
    },
    GRADUATED {
        @Override
        public boolean isHideInfo() {
            return false;
        }

        @Override
        public boolean isMember() {
            return true;
        }

        @Override
        public boolean isSelfToggleable() {
            return true;
        }

        @Override
        public String getDescription() {
            return "졸업회원";
        }

        @Override
        public List<MemberRole> getRequiredRoles() {
            return Collections.singletonList(MemberRole.MEMBER);
        }
    },
    COMPLETE {
        @Override
        public boolean isHideInfo() {
            return false;
        }

        @Override
        public boolean isMember() {
            return true;
        }

        @Override
        public boolean isSelfToggleable() {
            return true;
        }

        @Override
        public String getDescription() {
            return "수료회원";
        }

        @Override
        public List<MemberRole> getRequiredRoles() {
            return Collections.singletonList(MemberRole.MEMBER);
        }
    },
    INACTIVE {
        @Override
        public boolean isHideInfo() {
            return false;
        }

        @Override
        public boolean isMember() {
            return true;
        }

        @Override
        public boolean isSelfToggleable() {
            return true;
        }

        @Override
        public String getDescription() {
            return "한 학기 비활동";
        }

        @Override
        public List<MemberRole> getRequiredRoles() {
            return Collections.singletonList(MemberRole.MEMBER);
        }
    },
    MEMBER {
        @Override
        public boolean isHideInfo() {
            return false;
        }

        @Override
        public boolean isMember() {
            return true;
        }

        @Override
        public String getDescription() {
            return "일반회원";
        }

        @Override
        public List<MemberRole> getRequiredRoles() {
            return Collections.singletonList(MemberRole.MEMBER);
        }
    },
    UNACCEPTED {
        @Override
        public String getDescription() {
            return "승인전";
        }
    },
    EXPELLED {
        @Override
        public String getDescription() {
            return "자격상실";
        }
    },
    QUIT {
        @Override
        public boolean isSelfToggleable() {
            return true;
        }

        @Override
        public String getDescription() {
            return "자진탈퇴";
        }
    },
    PUBLIC {
        @Override
        public String getDescription() {
            return "외부인";
        }
    }
}
