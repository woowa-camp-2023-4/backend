package integration;

import com.woowa.woowakit.domain.product.dto.request.ProductCreateRequest;
import integration.helper.CommonRestAssuredUtils;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Product 인수 테스트")
class ProductIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("상품 이름, 가격, 이미지 주소를 입력해 Product를 생성할 수 있다.")
    void createWithName() {
        // given
        ProductCreateRequest request = ProductCreateRequest.of("test", 3000L, "testImage");

        // when
        ExtractableResponse<Response> response = CommonRestAssuredUtils.post("/products", request);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/products/1");
    }
}
