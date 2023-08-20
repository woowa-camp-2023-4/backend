package com.woowa.woowakit.domain.order.api;

import com.woowa.woowakit.domain.auth.annotation.Authenticated;
import com.woowa.woowakit.domain.auth.annotation.User;
import com.woowa.woowakit.domain.auth.domain.AuthPrincipal;
import com.woowa.woowakit.domain.order.application.OrderService;
import com.woowa.woowakit.domain.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.request.PreOrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.response.PreOrderResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/pre")
    @User
    public ResponseEntity<PreOrderResponse> createPreOrder(
        @Authenticated AuthPrincipal authPrincipal,
        @Valid @RequestBody PreOrderCreateRequest request
    ) {
        PreOrderResponse preOrderResponse = orderService.preOrder(authPrincipal, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(preOrderResponse);
    }

    @PostMapping()
    @User
    public ResponseEntity<Long> createOrder(
        @Authenticated AuthPrincipal authPrincipal,
        @Valid @RequestBody OrderCreateRequest request
    ) {
        Long orderId = orderService.order(authPrincipal, request);
        return ResponseEntity.status(HttpStatus.OK).body(orderId);
    }
}
