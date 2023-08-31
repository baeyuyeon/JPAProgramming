package jpabook.start.chapter8_1;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main8_1 {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {

            tx.begin();
            save(em);
            tx.commit();
            em.clear();
            find(em);
        } catch (Exception e) {
            tx.rollback();
        } finally {

            em.close();
        }
        emf.close();
    }

    public static void save(EntityManager em) {
        Team8_1 team = new Team8_1();
        team.setId("team1");
        team.setName("팀1");
        em.persist(team);

        Member8_1 member = new Member8_1();
        member.setId("yuyeon");
        member.setUsername("배유");
        member.setTeam(team);
        em.persist(member);
        
    }

    public static void find(EntityManager em) {
        Member8_1 member = em.find(Member8_1.class, "yuyeon");
        Team8_1 findTeam = member.getTeam();

    }
}
