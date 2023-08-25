package jpabook.start.chapter6_4_4;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Member644 {

    @Id
    @Column(name = "MEMBER_ID")
    private String id;

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //역방향
    @OneToMany(mappedBy = "member")
    private List<Order> memberProducts = new ArrayList<>();

    public List<Order> getMemberProducts() {
        return memberProducts;
    }

    public void setMemberProducts(List<Order> memberProducts) {
        this.memberProducts = memberProducts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
