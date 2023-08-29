package jpabook.start.chapter7_3_3;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class Child2 {

    @EmbeddedId
    private ChildId2 id;

    @MapsId("parentId")
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    public Parent2 parent;

    private String name;

}
