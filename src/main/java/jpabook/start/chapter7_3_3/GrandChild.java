package jpabook.start.chapter7_3_3;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class GrandChild {

    @EmbeddedId
    private GrandChildId id;

    @MapsId("childId")
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "PARENT_ID"),
            @JoinColumn(name = "CHILD_ID")
    })
    private Child2 child;

    private String name;
}
