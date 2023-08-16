package com.woowa.woowakit.domain.member.auth;

import com.woowa.woowakit.domain.member.domain.PasswordEncoder;
import com.woowa.woowakit.domain.member.exception.PasswordNotHashException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.springframework.stereotype.Component;

@Component
public class PBKDF2PasswordEncoder implements PasswordEncoder {

    @Override
    public boolean matches(String planePassword, String encodedPassword) {
        byte[] salt = getSalt(encodedPassword);
        String hashedPassword = encode(planePassword, salt);

        return encodedPassword.equals(hashedPassword);
    }

    private String encode(String password, byte[] salt) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            String base64Hash = Base64.getEncoder().encodeToString(hash);
            String base64Salt = Base64.getEncoder().encodeToString(salt);
            return base64Salt + base64Hash;
        } catch (Exception e) {
            throw new PasswordNotHashException(e);
        }
    }

    @Override
    public String encode(String password) {
        return encode(password, getSalt());
    }

    private byte[] getSalt() {
        Random random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private byte[] getSalt(String digest) {
        return Base64.getDecoder().decode(digest.substring(0, 24));
    }
}
