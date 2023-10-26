package jpabook.start;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 연관관계 매핑 핵심 키워드
 * <p>
 * 방향 - 단방향: 두 객체가 있을 때 한 쪽만 참조하는 경우 - 양방향: 두 객체가 있을 때 모두 서로 참조하는 경우
 * <p>
 * 다중성 - 다대일 -> ex. 여러 회원은 하나의 팀에 속할 수 있다 => 회원 : 팀 = 다 : 1 - 일대다 - 일대일 - 다대다
 * <p>
 * 연관관계 주인 - 객체를 양방향 연관관계로 만들면 주인을 정해야 함
 * <p>
 * 객체와 테이블 간 연관관계 차이
 * <p>
 * 객체: - 기본적으로 단방향 연관관계이며, 양방향을 위해 따로 작업해줘야 함 -> 서로 다른 단방향 관계 2개 - 참조를 활용한 연관관계 class A { B b; //
 * 참조(주소)로 연관관계 맺음 }
 * <p>
 * class B { A a;     // 양방향을 원하면 따로 참조 줘야 함 }
 * <p>
 * 테이블: - 기본적으로 외래키를 통한 JOIN으로 양방향 연관관계 - 외래 키를 활용한 연관관계 SELECT * FROM MEMBER M JOIN TEAM T ON
 * M.TEAM_ID = T.TEAM_ID(회원 -> 팀) SELECT * FROM TEAM T JOIN MEMBER M ON T.TEAM_ID = M.TEAM_ID(회원 ->
 * 팀)
 * <p>
 * 정리: - 단방향 매핑만으로 테이블과 객체의 연관관계 매핑은 이미 완료 - 단방향을 양방향ㅇ으로 만들면 반대방향으로 객체 그래프 탐색 기능이 추가도미 - 양방향 연관관계를
 * 매핑하려면 객체에서 양쪽 방향을 모두 관리해야 함
 * <p>
 * 연관관계 주인 정하는 기준 - 비즈니스 로직상 더 중요하다고 주인으로 판단하면 안됨 - 외래키 관리자 정도의 의미로만 부여해야 함
 */
public class JpaMain2 {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
    static EntityManager em = emf.createEntityManager();

    public static void main(String[] args) {

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            bidirection();
            tx.commit();
            em.clear();
            final Team team = em.find(Team.class, "team1");
            System.out.println("team.getName() = " + team.getName());
            team.getMembers().forEach(member -> {
                System.out.println("member.getUsername() = " + member.getUsername());
            });

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void insertSingle() {
        Team team1 = new Team();
        team1.setId("team1");
        team1.setName("팀1");
        em.persist(team1);

        Member member1 = new Member();
        member1.setId("member1");
        member1.setUsername("회원1");
        member1.setTeam(team1);
        em.persist(member1);

        Member member2 = new Member();
        member2.setId("member2");
        member2.setUsername("회원2");
        member2.setTeam(team1);
        em.persist(member2);
    }

    /**
     * 객체 그래프 탐색 방식: 객체를 통해 연관된 엔티티 조회
     */
    private static void query() {
        insertSingle();

        Member
                member = em.find(Member
                .class, "member1");
        Team team = member.getTeam();
        System.out.println("team.getId() = " + team.getId());
        System.out.println("team.getName() = " + team.getName());
    }

    /**
     * 객체지향 쿼리 사용
     */
    private static void queryLogicJoin() {
        insertSingle();

        final String jpql = "select m from Member"
                + " m join m.team t where t.name=:teamName";

        final List<Member
                > resultList = em.createQuery(jpql, Member
                        .class)
                .setParameter("teamName", "팀1")
                .getResultList();

        resultList.forEach(member -> {
            System.out.println("[query] member.username=" + member.getUsername());
        });
    }

    private static void update() {
        insertSingle();

        Team team2 = new Team();
        team2.setId("team2");
        team2.setName("팀2");
        em.persist(team2);

        Member
                member = em.find(Member
                .class, "member1");
        member.setTeam(team2);
    }

    private static void delete() {
        insertSingle();

        Team team1 = em.find(Team.class, "team1");

        Member
                member1 = em.find(Member
                .class, "member1");
        member1.setTeam(null);

        Member
                member2 = em.find(Member
                .class, "member2");
        member2.setTeam(null);

        // 팀에 대한 정보까지 완전히 삭제하려면
        // 연관관계를 모두 끊고 삭제해야 명
        member1.setTeam(null);
        member2.setTeam(null);
        em.remove(team1);
    }


    /**
     * WARNING: insertBi를 보면 setTeam을 통해 연관관계 주인인 Member에 연관관계를 맺는 방식을 시도했으나 동작 안함 오히려 Team에서
     * members에 추가, 즉 연관관계 주인이 아닌 객체에 참조해야 매핑이 되는 것을 확인할 수 있음. 레거시 버전 사용으로 인한 문제라 우선 추측, 책에 있는 내용대로
     * 기술.
     */
    private static void insertBi() {

        Team team1 = new Team();
        team1.setId("team1");
        team1.setName("팀1");
        em.persist(team1);

        Member member1 = new Member();
        member1.setId("member1");
        member1.setUsername("회원1");
        member1.setTeam(team1);
        //team1.getMembers().add(member1);
        em.persist(member1);

        Member member2 = new Member();
        member2.setId("member2");
        member2.setUsername("회원2");
        member2.setTeam(team1);
        //team1.getMembers().add(member2);
        em.persist(member2);
    }

    /**
     * 연관관계 주인: 객체에는 양방향 연관관계라는 것이 없음 => 서로 다른 단방향 연관관계 2개를 로직으로 묶어서 양방향 처리한 것 회원 -> 팀(단방향) 팀 ->
     * 회원(단방향) 반면 테이블에는 외래 키 하나로 양쪽 테이블 JOIN 가능 팀 <-> 회원(양방향) 여기서, 엔티티를 양방향 연관관계 설정 시 객체 참조는 둘이고, 외래
     * 키는 하나가 됨 => 둘 사이에 차이가 발생함 이런 차이로 인해 JPA에선 두 객체 연관관계 중 하나를 정해서 테이블의 외래 키를 관리해야 하는 객체가 필요 이것이
     * 바로 '연관관계 주인'
     * <p>
     * 양방향 매핑 규칙: 연관관계 주인만이 DB 연관관계와 매핑되고, 외래 키를 관리(CUD)가 가능 반면 주인 아닌 쪽은 읽기만 할 수 있음
     * <p>
     * Member & TeamBi 연관관계 매핑 시 이런식으로 된다'고' 함 team.getMembers().add(member1);     // 무시(연관관계 주인 아님)
     * team.getMembers().add(member1);     // 무시(연관관계 주인 아님) member1.setTeam(team);              //
     * 연관관계 설정(주인) member1.setTeam(team);              // 연관관계 설정(주인)
     */
    private static void bidirection() {
        insertBi();


    }

    static class PureMember {

        private PureTeam team;

        public void setTeam(PureTeam team) {
            if (this.team != null) {
                this.team.getMembers().remove(this);
            }
            this.team = team;
            this.team.getMembers().add(this);
        }
    }

    static class PureTeam {

        List<PureMember> members = new ArrayList<>();

        public List<PureMember> getMembers() {
            return members;
        }
    }

    private static void pureObjects() {
        PureTeam team = new PureTeam();
        PureMember member1 = new PureMember();
        PureMember member2 = new PureMember();

        member1.setTeam(team);
        //team.getMembers().add(member1);
        member2.setTeam(team);
        //team.getMembers().add(member2);
    }
}