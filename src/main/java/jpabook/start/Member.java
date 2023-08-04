package jpabook.start;

import javax.persistence.*;

@Entity
@Table(name="member")
public class Member {

    @Id
    @Column(name="id")
    private String id;
    @Column(name = "name")
    private String username;
    private Integer age; // 생략해도 매핑됨.

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
