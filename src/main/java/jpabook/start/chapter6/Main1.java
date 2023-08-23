package jpabook.start.chapter6;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main1 {

    public static void main(String[] args) {
        // 엔터티 매니저 팩토리 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        //엔터티 매니저 생성
        EntityManager em = emf.createEntityManager();
        //트랜잭션 - 획득
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            testSave(em);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    public static void testSave(EntityManager em) {
        Member1 member1 = new Member1("member1");
        Member1 member2 = new Member1("member2");

        Team1 team1 = new Team1("team1");
        team1.getMembers().add(member1);
        team1.getMembers().add(member2);
        em.persist(team1); // 순서를 위로 올려봐도 insert 후 update member1,member2 가 발생한다!
        em.persist(member1);
        em.persist(member2);
        //em.persist(team1); //

    }
}
