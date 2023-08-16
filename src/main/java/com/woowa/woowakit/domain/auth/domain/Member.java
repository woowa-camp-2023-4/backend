package com.woowa.woowakit.domain.auth.domain;

import com.woowa.woowakit.domain.auth.domain.converter.EmailConverter;
import com.woowa.woowakit.domain.auth.domain.converter.PasswordConverter;
import com.woowa.woowakit.domain.auth.exception.LoginFailException;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private Member(Email email, EncodedPassword encodedPassword, String name, Role role) {
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.name = name;
        this.role = role;
    }

    public static Member of(String email, EncodedPassword encodedPassword, String name) {
        return new Member(Email.from(email), encodedPassword, name, Role.USER);
    }

    public void validatePassword(String password, PasswordEncoder passwordEncoder) {
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
