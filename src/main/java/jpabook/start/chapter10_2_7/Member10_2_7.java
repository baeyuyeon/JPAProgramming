package jpabook.start.chapter10_2_7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Member10_2_7 {

    @Id
    @Column(name = "MEMBER_ID")
    private String id;
    private String username;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team10_2_7 team;

    public Member10_2_7() {
    }

    public Member10_2_7(String id, String username) {
        this.id = id;
        this.username = username;
    }

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

    public Team10_2_7 getTeam() {
        return team;
    }

    public void setTeam(Team10_2_7 team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "Member10_2_7{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", team=" + team +
                '}';
    }
}
