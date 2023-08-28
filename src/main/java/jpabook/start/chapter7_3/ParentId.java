package jpabook.start.chapter7_3;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ParentId implements Serializable {

    @Column(name = "PARENT_ID1")
    private String id;

    @Column(name = "PARENT_ID2")
    private String id2;

    public ParentId() {
    }

    public ParentId(String id, String id2) {
        this.id = id;
        this.id2 = id2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParentId)) {
            return false;
        }
        ParentId parentId = (ParentId) o;
        return id.equals(parentId.id) && id2.equals(parentId.id2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, id2);
    }
}
