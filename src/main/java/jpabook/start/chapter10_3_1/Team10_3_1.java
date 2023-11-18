package jpabook.start.chapter10_3_1;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Team10_3_1 {

    @Id
    @Column(name = "TEAM_ID")
    private String id;
    private String name;

    @OneToMany(mappedBy = "team")
    private List<Member10_3_1> members = new ArrayList<>();

    public Team10_3_1() {
    }

    public Team10_3_1(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public List<Member10_3_1> getMembers() {
        return members;
    }

    public void setMembers(List<Member10_3_1> members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Team10_3_1{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }
}
