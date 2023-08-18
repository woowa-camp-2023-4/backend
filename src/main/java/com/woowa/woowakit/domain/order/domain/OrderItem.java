package com.woowa.woowakit.domain.order.domain;

import com.woowa.woowakit.domain.model.BaseEntity;
import com.woowa.woowakit.domain.model.Image;
import com.woowa.woowakit.domain.model.Money;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.model.converter.ImageConverter;
import com.woowa.woowakit.domain.model.converter.MoneyConverter;
import com.woowa.woowakit.domain.model.converter.QuantityConverter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    @Convert(converter = ImageConverter.class)
    private Image image;

    @Column(name = "price")
    @Convert(converter = MoneyConverter.class)
    private Money price;

    @Convert(converter = QuantityConverter.class)
    private Quantity quantity;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_item_id")
    private final List<OrderItemStock> orderItemStocks = new ArrayList<>();

    @Builder
    private OrderItem(
        final Long productId,
        final String name,
        final Image image,
        final Money price,
        final Quantity quantity
    ) {
        this.productId = productId;
        this.name = name;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderItem of(
        final Long productId,
        final String name,
        final Image image,
        final Money price,
        final Quantity quantity
    ) {
        return OrderItem.builder()
            .productId(productId)
            .name(name)
            .image(image)
            .price(price)
            .quantity(quantity)
            .build();
    }

    public Money calculateTotalPrice() {
        return price.multiply(quantity.getQuantity());
    }
}
