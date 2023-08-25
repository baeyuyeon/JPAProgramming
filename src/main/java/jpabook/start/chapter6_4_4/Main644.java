package jpabook.start.chapter6_4_4;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main644 {

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
        // 회원 저장
        Member644 member = new Member644();
        member.setId("member1");
        member.setUsername("회원1");
        em.persist(member);

        //상품 저장
        Product644 product = new Product644();
        product.setId("productA");
        product.setName("상품A");
        em.persist(product);

        //회원 상품 저장
        Order order = new Order();
        order.setMember(member);
        order.setProduct(product);
        order.setOrderAmount(10000);

        em.persist(order);
    }

    public static void find(EntityManager em) {
        // 기본 키 값 생성
        Long orderId = 1L;

        Order order = em.find(Order.class, orderId);

        Member644 member = order.getMember();
        Product644 product = order.getProduct();

        System.out.println("member = " + member.getUsername());
        System.out.println("product = " + product.getName());
        System.out.println("order amount = " + order.getOrderAmount());
    }
}
