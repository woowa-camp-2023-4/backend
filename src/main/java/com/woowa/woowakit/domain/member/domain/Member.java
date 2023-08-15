package com.woowa.woowakit.domain.member.domain;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "members")
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

    private Member(Email email, EncodedPassword encodedPassword, String name) {
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.name = name;
    }

    public static Member of(String email, EncodedPassword encodedPassword, String name) {
        return new Member(Email.from(email), encodedPassword, name);
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
