package jpabook.start.chapter8_4;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Child8_4 {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Parent8_4 parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Parent8_4 getParent() {
        return parent;
    }

    public void setParent(Parent8_4 parent) {
        this.parent = parent;
    }
}
