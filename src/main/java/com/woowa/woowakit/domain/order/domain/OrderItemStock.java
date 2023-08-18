package com.woowa.woowakit.domain.order.domain;

import com.woowa.woowakit.domain.model.BaseEntity;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.model.converter.QuantityConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_item_stocks")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class OrderItemStock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_id")
    private Long stockId;

    @Column(name = "quantity")
    @Convert(converter = QuantityConverter.class)
    private Quantity quantity;

    private OrderItemStock(Long stockId, Quantity quantity) {
        this.stockId = stockId;
        this.quantity = quantity;
    }

    public static OrderItemStock of(Long stockId, Quantity quantity) {
        return new OrderItemStock(stockId, quantity);
    }
}
