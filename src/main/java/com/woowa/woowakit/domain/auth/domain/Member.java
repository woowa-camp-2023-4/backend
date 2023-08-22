package com.woowa.woowakit.domain.auth.domain;

import com.woowa.woowakit.domain.auth.domain.converter.EmailConverter;
import com.woowa.woowakit.domain.auth.domain.converter.PasswordConverter;
import com.woowa.woowakit.domain.auth.exception.LoginFailException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "MEMBERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    @Convert(converter = EmailConverter.class)
    private Email email;

    @Column(name = "password", nullable = false)
    @Convert(converter = PasswordConverter.class)
    private EncodedPassword encodedPassword;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    private Member(
            final Email email,
            final EncodedPassword encodedPassword,
            final String name,
            final Role role
    ) {
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.name = name;
        this.role = role;
    }

    public static Member of(
            final String email,
            final EncodedPassword encodedPassword,
            final String name
    ) {
        return new Member(Email.from(email), encodedPassword, name, Role.USER);
    }

    public void validatePassword(final String password, final PasswordEncoder passwordEncoder) {
        if (!encodedPassword.isMatch(password, passwordEncoder)) {
            throw new LoginFailException();
        }
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email=" + email +
                ", encodedPassword=" + encodedPassword +
                ", name='" + name + '\'' +
                '}';
    }
}
