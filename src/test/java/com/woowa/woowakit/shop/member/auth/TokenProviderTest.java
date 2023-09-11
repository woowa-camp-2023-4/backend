package com.woowa.woowakit.shop.member.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowa.woowakit.shop.auth.infra.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TokenProvider 테스트")
class TokenProviderTest {

    private final TokenProvider tokenProvider = new TokenProvider("woowatechcamp", 10000);

    @Test
    @DisplayName("토큰 생성 테스트")
    void create() {
        String jwt = tokenProvider.createToken("hello");
        assertThat(jwt.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("토큰 파싱 테스트")
    void parse() {
        String token
            = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ3b293YSIsIm5hbWUiOiJKb2huIERvZSIsImlhdCI6OTk5OTk5OTk5OX0.-5yUrTOn4Zg0uqy_iWrToXqpE1-WhYp2K3YYxAssSoA";
        String payload = tokenProvider.getPayload(token);

        assertThat(payload).isEqualTo("woowa");
    }

    @Test
    @DisplayName("토큰 유효기간이 지나면 false를 반환한다")
    void expire() {
        String expiredToken
            = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoZWxsbyIsImlhdCI6MTY5MDg3MjA2NCwiZXhwIjoxMDB9.4nyUPi85TfzGELCJJTFTiEAhbH8BAL3x9_Qin6ELpVo";

        assertThat(tokenProvider.validateToken(expiredToken)).isFalse();
    }

    @Test
    @DisplayName("토큰 secret key가 다르면 false를 반환한다")
    void secretKey() {
        String invalidKeyToken
            = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoZWxsbyIsImlhdCI6MTY5MDg3MjA2NCwiZXhwIjo5OTk5OTk5OTk5fQ.40XK3K6WtgN3JlhpXx_SZJ4sev4ll5bglSabKnhIn10";

        assertThat(tokenProvider.validateToken(invalidKeyToken)).isFalse();
    }
}

