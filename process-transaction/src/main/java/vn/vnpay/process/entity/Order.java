package vn.vnpay.process.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Project: demo-payment
 * Package: vn.vnpay.process.entity
 * Author: zovivo
 * Date: 9/9/2021
 * Created with IntelliJ IDEA
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    private Long amount;
    private String description;
    @Column(name = "order_code")
    private String orderCode;
    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    public Order(Long amount, String description, String orderCode) {
        this.amount = amount;
        this.description = description;
        this.orderCode = orderCode;
    }
}
