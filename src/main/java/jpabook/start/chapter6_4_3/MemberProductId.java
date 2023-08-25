package jpabook.start.chapter6_4_3;

import java.io.Serializable;
import java.util.Objects;

public class MemberProductId implements Serializable {

    private String member;
    private String product;

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberProductId)) {
            return false;
        }
        MemberProductId that = (MemberProductId) o;
        return Objects.equals(member, that.member) && Objects.equals(product,
                that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, product);
    }
}
