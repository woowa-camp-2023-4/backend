package com.woowa.woowakit.domain.member.application;

import com.woowa.woowakit.domain.member.domain.EncodedPassword;
import com.woowa.woowakit.domain.member.domain.Member;
import com.woowa.woowakit.domain.member.domain.MemberRepository;
import com.woowa.woowakit.domain.member.domain.PasswordEncoder;
import com.woowa.woowakit.domain.member.dto.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthSerivce {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;


    public Long signUp(final SignUpRequest request) {
        passwordValidator.validatePassword(request.getPassword());

        Member member = Member.of(
            request.getEmail(),
            EncodedPassword.of(request.getPassword(), passwordEncoder),
            request.getName()
        );

        return memberRepository.save(member).getId();
    }
}
