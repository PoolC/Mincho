package org.poolc.api.batch.itemprocessor;

import org.poolc.api.auth.infra.PasswordHashProvider;
import org.poolc.api.batch.vo.member.MemberDao;
import org.poolc.api.batch.vo.member.Members;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class MembersItemProcessor implements ItemProcessor<Members, MemberDao> {
    @Autowired
    PasswordHashProvider passwordHashProvider;

    @Override
    public MemberDao process(final Members members) throws Exception {
        MemberDao member = new MemberDao();
        member.of(members, passwordHashProvider.encodePassword(members.getStudentId()));
        return member;
    }
}
