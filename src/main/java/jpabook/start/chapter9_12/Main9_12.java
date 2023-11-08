package jpabook.start.chapter9_12;

import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main9_12 {

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
            /*tx.begin();
            getMemberData(em);
            tx.commit();
            em.clear();*/
            tx.begin();
            update(em);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void getMemberData(EntityManager em) {
        Member9_12 member = em.find(Member9_12.class, 1L);

        Address homeAddress = member.getHomeAddress();

        System.out.println("homeAddress = " + homeAddress);

        Set<String> favoriteFoods = member.getFavoriteFoods();
        System.out.println("-----------------------------------");
        for (String food : favoriteFoods) {
            System.out.println("food = " + food);
        }

        List<Address> addressHistory = member.getAddressHistory();

        System.out.println("-----------------------------------");
        System.out.println("addressHistory = " + addressHistory.get(0));

    }


    private static void save(EntityManager em) {
        Member9_12 member = new Member9_12();
        //임베디드 값 타입
        member.setHomeAddress(new Address("통양", "몽돌해수욕장", "6666-4444"));

        // 기본값 타입 컬렉션
        member.getFavoriteFoods().add("짬뽕");
        member.getFavoriteFoods().add("피자");
        member.getFavoriteFoods().add("삼겹살");

        //임베디드 값 타입 컬렉션
        member.getAddressHistory().add(new Address("서울", "강남", "02-1111-2222"));
        member.getAddressHistory().add(new Address("동두천", "생연로", "031-444-5555"));

        em.persist(member);
    }

    private static void update(EntityManager em) {
        Member9_12 member = em.find(Member9_12.class, 1L);
        //임베디드 값 타입 수정
        member.setHomeAddress(new Address("통영22", "신도시", "123123"));

        //기본 값 타입 컬렉션 수정
        Set<String> favoriteFoods = member.getFavoriteFoods();
        favoriteFoods.remove("짬뽕");
        favoriteFoods.add("엄마집밥");

        //임베디드 값 타입 컬렉션 수정
        List<Address> addressHistory = member.getAddressHistory();
        addressHistory.remove(new Address("서울", "강남", "02-1111-2222"));
        addressHistory.add(new Address("서울", "신도림", "02-1111-2222"));
        
        //Address get강남ddress = addressHistory.get(0);
        //get강남ddress.

    }
}
