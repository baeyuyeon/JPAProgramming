package jpabook.start.chapter6_4;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main4 {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            save(em);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    public static void save(EntityManager em) {
        Product product = new Product();
        product.setName("상품A");
        em.persist(product);

        Member4 member4 = new Member4();
        member4.setUsername("회원4");

        member4.getProducts().add(product);

        em.persist(member4);

        Member4 findMember = em.find(Member4.class, member4.getId());
        System.out.println("findMember : " + findMember.getId());
        List<Product> findProducts = findMember.getProducts();
        for (Product item : findProducts) {
            System.out.println("itemName : " + item.getName());
        }
    }

}
