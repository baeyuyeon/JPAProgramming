package jpabook.start.chapter7_3;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main7_3 {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {

            tx.begin();
            test(em);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {

            em.close();
        }
        emf.close();

    }

    public static void test(EntityManager em) {
        ParentId parentId = new ParentId("myId1", "myId2");

        Parent parent = new Parent();
        parent.setId(parentId);
        parent.setName("네임");
        em.persist(parent);

        ParentId findParentId = new ParentId("myId1", "myId2");
        Parent findParent = em.find(Parent.class, findParentId);
        System.out.println("findParent = " + findParent);
    }

}
