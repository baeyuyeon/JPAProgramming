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
            Long memberId = save(em);
            tx.commit();
            em.clear();
            find(em, memberId);
            // findInverse(em);

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    public static Long save(EntityManager em) {
        Product product = new Product();
        product.setName("상품A");
        em.persist(product);

        Product product2 = new Product();
        product2.setName("상품B");
        em.persist(product2);

        Member4 member4 = new Member4();
        member4.setUsername("회원4");

        //1번째 방법
        member4.getProducts().add(product);
        product.getMembers().add(member4);
        //member4.addProduct(product);
        //2번째 방법
        member4.addProduct(product2);

        em.persist(member4);

        return member4.getId();

    }

    public static void find(EntityManager em, Long memberId) {
        Member4 findMember = em.find(Member4.class, memberId);
        System.out.println("findMember : " + findMember.getId());
        List<Product> findProducts = findMember.getProducts();
        for (Product item : findProducts) {
            System.out.println("itemName : " + item.getName());
        }
    }

    public static void findInverse(EntityManager em) {
        Product product = em.find(Product.class, 1L);
        List<Member4> members = product.getMembers();
        for (Member4 entityMember : members) {
            System.out.println("entityMember = " + entityMember.getUsername());
        }
    }

}
