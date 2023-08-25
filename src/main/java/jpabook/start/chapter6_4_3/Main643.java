package jpabook.start.chapter6_4_3;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main643 {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            save(em);
            tx.commit();
            em.clear();
            findByMemberProductId(em);
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    public static void save(EntityManager em) {
        // 회원 저장
        Member643 member = new Member643();
        member.setId("member1");
        member.setUsername("회원1");
        em.persist(member);

        //상품 저장
        Product643 product = new Product643();
        product.setId("productA");
        product.setName("상품A");
        em.persist(product);

        //회원 상품 저장
        MemberProduct memberProduct = new MemberProduct();
        memberProduct.setMember(member);
        memberProduct.setProduct(product);
        memberProduct.setOrderAmount(10000);

        em.persist(memberProduct);
    }

    public static void findByMemberProductId(EntityManager em) {
        // 기본 키 값 생성
        MemberProductId memberProductId = new MemberProductId();
        memberProductId.setMember("member1");
        memberProductId.setProduct("productA");

        MemberProduct memberProduct = em.find(MemberProduct.class, memberProductId);

        Member643 member = memberProduct.getMember();
        Product643 product = memberProduct.getProduct();

        System.out.println("member = " + member.getUsername());
        System.out.println("product = " + product.getName());
        System.out.println("order amount = " + memberProduct.getOrderAmount());
    }
}
