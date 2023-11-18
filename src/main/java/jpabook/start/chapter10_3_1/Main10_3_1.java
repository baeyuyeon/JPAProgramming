package jpabook.start.chapter10_3_1;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class Main10_3_1 {

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
            criteriaBasic(em);
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void criteriaBasic(EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member10_3_1> cq = cb.createQuery(Member10_3_1.class);

        Root<Member10_3_1> m = cq.from(Member10_3_1.class); // FROM절
        cq.select(m); //SELECT 절

        TypedQuery<Member10_3_1> query = em.createQuery(cq);
        List<Member10_3_1> members = query.getResultList();

        for (Member10_3_1 member : members) {
            System.out.println("member = " + member);
        }
    }

    private static void save(EntityManager em) {
        Member10_3_1 member1 = new Member10_3_1("member1", "멤버일");
        Member10_3_1 member2 = new Member10_3_1("member2", "멤버이");
        Member10_3_1 member3 = new Member10_3_1("member3", "멤버삼");

        Member10_3_1 member4 = new Member10_3_1("member4", "멤버사");
        Member10_3_1 member5 = new Member10_3_1("member5", "멤버오");
        Member10_3_1 member6 = new Member10_3_1("member6", "멤버육");

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        em.persist(member5);
        em.persist(member6);

        Team10_3_1 team = new Team10_3_1("team1", "팀A");
        em.persist(team);
        Team10_3_1 team2 = new Team10_3_1("team2", "팀B");
        em.persist(team2);
        Team10_3_1 team3 = new Team10_3_1("team3", "팀C");
        em.persist(team3);

        member1.setTeam(team);
        member2.setTeam(team2);
        member3.setTeam(team3);

        member4.setTeam(team);
        member5.setTeam(team2);
        member6.setTeam(team3);

    }


}
