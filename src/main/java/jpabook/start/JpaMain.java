package jpabook.start;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        // [엔터티 매니저 팩토리 생성] - 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        // [엔터티 매니저] - 생성
        EntityManager em = emf.createEntityManager();
        // [트랜잭션] - 획득
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            logic(em);
            //logic2(em);
            //testSave(em);
            /*queryLogicJoin(em);
            updateRelation(em);
            deleteRelation(em);*/
            //bindDirection(em);
            //testSaveNonOwner(em);
            //testORM_양방향(em);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void logic(EntityManager em) {
        String id = "id1";
        Member member = new Member();
        member.setId(id);
        member.setUsername("배유연");
        member.setAge(30);

        //등록
        em.persist(member);

        //수정
        member.setAge(20);

        //한 건 조회
        Member findMember = em.find(Member.class, id);
        System.out.println(
                "findMember = " + findMember.getUsername() + ", age = " + findMember.getAge());

        //목록 조회
        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();
        System.out.println("member.size = " + members.size());

        //삭제
        em.remove(member);

    }

    private static void logic2(EntityManager em) {
        Board board = new Board();
        em.persist(board);
        System.out.println("board.id = " + board.getId());
    }

    private static void testSave(EntityManager em) {
        Team team1 = new Team("team1", "팀1");
        em.persist(team1);

        //회원1 저장
        Member member1 = new Member("member1", "회원1");
        member1.setTeam(team1);
        em.persist(member1);

        //회원2 저장
        Member member2 = new Member("member2", "회원2");
        member2.setTeam(team1);
        em.persist(member2);

    }

    private static void queryLogicJoin(EntityManager em) {
        String jpql = "select m from Member m join m.team t where " + "t.name=:teamName";
        List<Member> resultList = em.createQuery(jpql, Member.class)
                .setParameter("teamName", "팀1")
                .getResultList();
        for (Member member : resultList) {
            System.out.println("member 명= " + member.getUsername());
        }

    }

    private static void updateRelation(EntityManager em) {
        Team team2 = new Team("team2", "팀2");
        em.persist(team2);

        Member member = em.find(Member.class, "member1");
        member.setTeam(team2);
    }

    private static void deleteRelation(EntityManager em) {
        Member member1 = em.find(Member.class, "member1");
        member1.setTeam(null);
        Member member2 = em.find(Member.class, "member2");
        member2.setTeam(null);

        Team team1 = em.find(Team.class, "team1");
        em.remove(team1);
    }


    public static void bindDirection(EntityManager em) {
        Team team = em.find(Team.class, "team1");
        List<Member> members = team.getMembers();// (팀 -> 회원), 객체 그래프 탐색
        for (Member member : members) {
            System.out.println("member.username = " + member.getUsername());
        }
    }


    //주인이 아닌곳에 연관관계 설정하면? 외래키저장 X
    public static void testSaveNonOwner(EntityManager em) {
        //회원1 저장
        Member member1 = new Member("member1", "회원1");
        em.persist(member1);

        //회원2 저장
        Member member2 = new Member("member2", "회원2");
        em.persist(member2);

        Team team1 = new Team("team1", "팀1");
        //주인이 아닌 곳만 연관관계 설정
        team1.getMembers().add(member1);
        team1.getMembers().add(member2);

        em.persist(team1);

    }

    public static void testORM_양방향(EntityManager em) {
        //팀 1 저장
        Team team1 = new Team("team1", "팀1");
        em.persist(team1);

        Member member1 = new Member("member1", "회원1");

        //양방향 연관관계 설정
        member1.setTeam(team1);
        em.persist(member1);

        Member member2 = new Member("member2", "회원2");

        //양방향 연관관계 설정
        member2.setTeam(team1);
        em.persist(member2);

    }
}
