package jpabook.start.chapter7_3_3;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GrandChildId implements Serializable {

    private ChildId2 childId;

    @Column(name = "GRANDCHILD_ID")
    private String id;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GrandChildId)) {
            return false;
        }
        GrandChildId that = (GrandChildId) o;
        return childId.equals(that.childId) && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(childId, id);
    }
}
