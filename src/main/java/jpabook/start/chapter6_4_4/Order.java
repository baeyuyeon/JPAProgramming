package jpabook.start.chapter6_4_4;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member644 member;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product644 product;

    private int orderAmount;

    public Long getId() {
        return id;
    }

    public Member644 getMember() {
        return member;
    }

    public void setMember(Member644 member) {
        this.member = member;
    }

    public Product644 getProduct() {
        return product;
    }

    public void setProduct(Product644 product) {
        this.product = product;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }
}
