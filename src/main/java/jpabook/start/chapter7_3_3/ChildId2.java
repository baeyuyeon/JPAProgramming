package jpabook.start.chapter7_3_3;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ChildId2 implements Serializable {

    private String parentId;

    @Column(name = "CHILD_ID")
    private String id;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChildId2)) {
            return false;
        }
        ChildId2 childId2 = (ChildId2) o;
        return parentId.equals(childId2.parentId) && id.equals(childId2.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentId, id);
    }
}
