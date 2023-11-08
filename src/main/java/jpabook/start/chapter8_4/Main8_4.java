package jpabook.start.chapter8_4;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main8_4 {

    public static void main(String[] args) {
        // [엔터티 매니저 팩토리 생성] - 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        // [엔터티 매니저] - 생성
        EntityManager em = emf.createEntityManager();
        // [트랜잭션] - 획득
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Long id = saveWithCascade(em);
            tx.commit();
            em.clear();
            tx.begin();
            remove(em, id);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void remove(EntityManager em, Long id) {
        Parent8_4 findParent = em.find(Parent8_4.class, id);
        System.out.println("findParent = " + findParent.getId());
        List<Child8_4> children = findParent.getChildren();
        children.get(0).setParent(null);
        children.get(1).setParent(null);

        findParent.getChildren().remove(0);

        //em.remove(findParent);

    }

    private static Long saveWithCascade(EntityManager em) {
        Child8_4 child1 = new Child8_4();
        Child8_4 child2 = new Child8_4();

        Parent8_4 parent = new Parent8_4();
        child1.setParent(parent);
        child2.setParent(parent);

        parent.getChildren().add(child1);
        parent.getChildren().add(child2);

        em.persist(parent);

        return parent.getId();
    }
}
