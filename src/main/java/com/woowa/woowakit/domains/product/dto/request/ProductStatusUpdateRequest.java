package com.woowa.woowakit.domains.product.dto.request;

import com.woowa.woowakit.domains.product.domain.product.ProductStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProductStatusUpdateRequest {

    @NotNull(message = "상품 상태는 필수입니다.")
    private ProductStatus productStatus;

    public static ProductStatusUpdateRequest of(final ProductStatus productStatus) {
        return new ProductStatusUpdateRequest(productStatus);
    }
}
