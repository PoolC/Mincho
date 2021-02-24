package org.poolc.api.auth.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PoolCMemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginID) throws UsernameNotFoundException {
        return memberRepository.findByLoginID(loginID)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Member %s does not exist", loginID)));
    }
}
