package jpabook.start.chapter6_4_3;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@IdClass(MemberProductId.class)
public class MemberProduct {

    @Id
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member643 member;

    @Id
    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product643 product;

    private int orderAmount;

    public Member643 getMember() {
        return member;
    }

    public void setMember(Member643 member) {
        this.member = member;
    }

    public Product643 getProduct() {
        return product;
    }

    public void setProduct(Product643 product) {
        this.product = product;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }
}
