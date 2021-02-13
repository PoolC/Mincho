package org.poolc.api.member.repository;

import org.poolc.api.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {

    Member findByLoginID(String loginID);

    boolean existsByLoginIDOrEmailOrPhoneNumberOrStudentID(String loginID, String email, String phoneNumber, String studentID);
}