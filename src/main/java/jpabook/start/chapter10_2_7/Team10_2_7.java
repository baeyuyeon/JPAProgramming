package jpabook.start.chapter10_2_7;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Team10_2_7 {

    @Id
    @Column(name = "TEAM_ID")
    private String id;
    private String name;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<Member10_2_7> members = new ArrayList<>();

    public Team10_2_7() {
    }

    public Team10_2_7(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public List<Member10_2_7> getMembers() {
        return members;
    }

    public void setMembers(List<Member10_2_7> members) {
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

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Team10_2_7{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
