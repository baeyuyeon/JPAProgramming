package jpabook.start.chapter10_1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "MEMBER10_1")
public class Member10_1 {

    @Id
    private String memberId;

    @Column(name = "name")
    private String username;

    public Member10_1(String memberId, String username) {
        this.memberId = memberId;
        this.username = username;
    }

    public Member10_1() {
    }

    @Override
    public String toString() {
        return "Member10_1{" +
                "memberId='" + memberId + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
