package integration.product;


import static org.assertj.core.api.Assertions.assertThat;

import com.woowa.woowakit.domain.product.application.ImageUploader;
import integration.IntegrationTest;
import integration.helper.MemberHelper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;


@DisplayName("ProductImage 인수 테스트")
class ProductImageIntegrationTest extends IntegrationTest {

    @MockBean
    private ImageUploader imageUploader;

    @Test
    @DisplayName("관리자는 상품 이미지를 등록할 수 있다.")
    void uploadProductImage() throws IOException {
        //given
        Mockito.when(imageUploader.upload(Mockito.any())).thenReturn("imagePath");
        String accessToken = MemberHelper.login(MemberHelper.createAdminLoginRequest());
        MockMultipartFile file = new MockMultipartFile("data", "test.png",
            MediaType.IMAGE_PNG_VALUE, "test".getBytes());

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .multiPart("data", "data", file.getBytes())
            .when()
            .post("/products/images")
            .then().log().all().extract();

        //then
        assertThat(response.statusCode()).isEqualTo(200);
    }
}
