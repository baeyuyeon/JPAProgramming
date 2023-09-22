package jpabook.start.chapter10_2_7;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main10_2_7 {

    public static void main(String[] args) {
        // [엔터티 매니저 팩토리 생성] - 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        // [엔터티 매니저] - 생성
        EntityManager em = emf.createEntityManager();
        // [트랜잭션] - 획득
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            save(em);
            tx.commit();
            em.clear();
            tx.begin();
            getMemberData(em);
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void getMemberData(EntityManager em) {
        Team10_2_7 team10_2_7 = em.find(Team10_2_7.class, "team1");
        System.out.println("team10_2_7 = " + team10_2_7);
        String jpql = "select t from Team10_2_7 t join t.members where t.name='팀A'";

        List<Team10_2_7> resultList = em.createQuery(jpql, Team10_2_7.class).getResultList();
        resultList.stream().forEach(a -> System.out.println("team = " + a));
    }

    private static void save(EntityManager em) {
        Member10_2_7 member1 = new Member10_2_7("member1", "배유연");
        Member10_2_7 member2 = new Member10_2_7("member2", "배유연");
        Member10_2_7 member3 = new Member10_2_7("member3", "이지후");

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);

        Team10_2_7 team = new Team10_2_7("team1", "팀A");
        em.persist(team);

        member1.setTeam(team);
        member2.setTeam(team);
        member3.setTeam(team);

    }
}
