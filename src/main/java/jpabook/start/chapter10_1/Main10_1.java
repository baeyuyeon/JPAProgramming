package jpabook.start.chapter10_1;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class Main10_1 {

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
            //getMemberData(em);
            //getMemberDataUseCriteria(em);
            paging(em);
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void getMemberData(EntityManager em) {
        String jpql = "select m from Member10_1 as m where m.username like '배유연%'";

        List<Member10_1> resultList = em.createQuery(jpql, Member10_1.class).getResultList();
        resultList.stream().forEach(a -> System.out.println("member = " + a));
    }

    private static void getMemberDataUseCriteria(EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member10_1> query = cb.createQuery(Member10_1.class);

        //루트클래스(조회를 시작할 클래스)
        Root<Member10_1> m = query.from(Member10_1.class);

        //쿼리 생성
        CriteriaQuery<Member10_1> cq = query.select(m).where(cb.equal(m.get("username"), "배유연"));
        List<Member10_1> resultList = em.createQuery(cq).getResultList();
        resultList.stream().forEach(a -> System.out.println("member = " + a));

    }

    private static void save(EntityManager em) {
        Member10_1 member1 = new Member10_1("member1", "배유연");
        Member10_1 member2 = new Member10_1("member2", "배유연");
        Member10_1 member3 = new Member10_1("member3", "이지후");

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
    }

    private static void paging(EntityManager em) {
        TypedQuery<Member10_1> query = em.createQuery(
                "SELECT m FROM Member10_1 m ORDER BY m.username DESC", Member10_1.class);
        query.setFirstResult(10);
        query.setMaxResults(20);
        List<Member10_1> resultList = query.getResultList();
    }


}
