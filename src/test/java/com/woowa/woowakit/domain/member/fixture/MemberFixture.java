package com.woowa.woowakit.domain.member.fixture;

import static com.woowa.woowakit.domain.auth.domain.Member.*;

import com.woowa.woowakit.domain.auth.domain.Email;
import com.woowa.woowakit.domain.auth.domain.EncodedPassword;
import com.woowa.woowakit.domain.auth.domain.Member;
import com.woowa.woowakit.domain.auth.domain.Role;

public class MemberFixture {

	public static MemberBuilder anMember() {
		return Member.builder()
			.name("탐탐")
			.email(Email.from("jiwonjjang@jiwon.com"))
			.encodedPassword(EncodedPassword.from("hellojiwon1234"))
			.role(Role.USER);
	}
}
