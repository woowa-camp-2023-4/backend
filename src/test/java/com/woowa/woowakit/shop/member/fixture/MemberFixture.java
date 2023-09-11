package com.woowa.woowakit.shop.member.fixture;

import static com.woowa.woowakit.shop.auth.domain.Member.*;

import com.woowa.woowakit.shop.auth.domain.Email;
import com.woowa.woowakit.shop.auth.domain.EncodedPassword;
import com.woowa.woowakit.shop.auth.domain.Member;
import com.woowa.woowakit.shop.auth.domain.Role;

public class MemberFixture {

	public static MemberBuilder anMember() {
		return Member.builder()
			.name("탐탐")
			.email(Email.from("jiwonjjang@jiwon.com"))
			.encodedPassword(EncodedPassword.from("hellojiwon1234"))
			.role(Role.USER);
	}
}
