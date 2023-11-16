package jpabook.start.chapter10_2_7;


public class TeamDTO {

    private String id;
    private String name;
    private String memberId;
    private String memberName;

    public TeamDTO(String id, String name, String memberId, String memberName) {
        this.id = id;
        this.name = name;
        this.memberId = memberId;
        this.memberName = memberName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    @Override
    public String toString() {
        return "TeamDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", memberId='" + memberId + '\'' +
                ", memberName='" + memberName + '\'' +
                '}';
    }
}
