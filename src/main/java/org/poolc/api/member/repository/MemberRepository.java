package org.poolc.api.member.repository;

import org.poolc.api.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByLoginID(String loginID);

    List<Member> findByName(String name);
    
    boolean existsByLoginIDOrEmailOrPhoneNumberOrStudentID(String loginID, String email, String phoneNumber, String studentID);
}