package jpabook.start.chapter10_3_1;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class Main10_3_1 {

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
            //criteriaBasic(em);
            //criteriaSelect(em);
            //criteriaConstruct(em);
            //criteriaTuple(em);
            //criteriaGroupBy(em);
            //criteriaJoin(em);
            criteriaFetch(em);
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void criteriaBasic(EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member10_3_1> cq = cb.createQuery(Member10_3_1.class);

        Root<Member10_3_1> m = cq.from(Member10_3_1.class); // FROM절
        //cq.select(m); //SELECT 절
        Predicate usernameEqual = cb.equal(m.get("username"), "멤버사");

        Predicate ageGt = cb.greaterThan(m.<Integer>get("age"), 10);

        Order ageDesc = cb.desc(m.get("age"));

        cq.select(m).where(usernameEqual, ageGt).orderBy(ageDesc);

        TypedQuery<Member10_3_1> query = em.createQuery(cq);
        List<Member10_3_1> members = query.getResultList();

        for (Member10_3_1 member : members) {
            System.out.println("member = " + member);
        }
    }

    private static void criteriaSelect(EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);

        Root<Member10_3_1> m = cq.from(Member10_3_1.class); // FROM절

        cq.multiselect(m.get("username"), m.get("age")).distinct(true);

        List<Object[]> objects = em.createQuery(cq).getResultList();

        System.out.println("objects = " + objects);
        Iterator iterator = objects.iterator();
        while (iterator.hasNext()) {
            Object[] obj = (Object[]) iterator.next();
            System.out.println("obj = " + Arrays.toString(obj));
            String username = (String) obj[0];
            int age = (int) obj[1];
            System.out.println("username = " + username + ", age = " + age);
        }

    }

    private static void criteriaConstruct(EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<MemberDTO> cq = cb.createQuery(MemberDTO.class);
        Root<Member10_3_1> m = cq.from(Member10_3_1.class);
        cq.select(cb.construct(MemberDTO.class, m.get("username"), m.get("age")));

        TypedQuery<MemberDTO> query = em.createQuery(cq);
        List<MemberDTO> resultList = query.getResultList();
        for (MemberDTO dto : resultList) {
            System.out.println("dto = " + dto);
        }
    }

    private static void criteriaTuple(EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        //CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class); // 위와 같다.

        Root<Member10_3_1> m = cq.from(Member10_3_1.class);
        cq.multiselect(m.get("username").alias("username"), // 튜플에서 사용할 튜플 별칭
                m.get("age").alias("age"));

        TypedQuery<Tuple> query = em.createQuery(cq);
        List<Tuple> resultList = query.getResultList();
        for (Tuple tuple : resultList) {
            //튜플 별칭으로 조회
            String username = tuple.get("username", String.class);
            int age = tuple.get("age", Integer.class);

            System.out.println("username = " + username + ", age = " + age);
        }
    }

    private static void criteriaGroupBy(EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Member10_3_1> m = cq.from(Member10_3_1.class);

        Expression maxAge = cb.max(m.<Integer>get("age"));
        Expression minAge = cb.min(m.<Integer>get("age"));

        cq.multiselect(m.get("team").get("name"), maxAge, minAge); // 팀이름별로 나이가 젤 많은사람, 적은사람
        cq.groupBy(m.get("team").get("name"))
                .having(cb.gt(minAge, 10));//10살이 초과하는 팀을 조회

        List<Object[]> objects = em.createQuery(cq).getResultList();

        Iterator iterator = objects.iterator();
        while (iterator.hasNext()) {
            Object[] obj = (Object[]) iterator.next();
            String username = (String) obj[0];
            int age = (int) obj[1];
            System.out.println("username = " + username + ", age = " + age);
        }

    }

    private static void criteriaJoin(EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);

        Root<Member10_3_1> m = cq.from(Member10_3_1.class);
        Join<Member10_3_1, Team10_3_1> t = m.join("team", JoinType.INNER);

        cq.multiselect(m, t).where(cb.equal(t.get("name"), "팀A"));

        List<Object[]> objects = em.createQuery(cq).getResultList();

        Iterator iterator = objects.iterator();
        while (iterator.hasNext()) {
            Object[] obj = (Object[]) iterator.next();
            Member10_3_1 member = (Member10_3_1) obj[0];
            Team10_3_1 team = (Team10_3_1) obj[1];
            System.out.println("member = " + member + ", team = " + team);
        }
    }

    private static void criteriaFetch(EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);

        Root<Member10_3_1> m = cq.from(Member10_3_1.class);
        //Join<Member10_3_1, Team10_3_1> t = m.join("team", JoinType.INNER);
        m.fetch("team", JoinType.LEFT);

        cq.multiselect(m).where(cb.equal(m.get("team").get("name"), "팀A"));

        List<Object[]> objects = em.createQuery(cq).getResultList();

        System.out.println("objects = " + objects);
    }

    private static void save(EntityManager em) {
        Member10_3_1 member1 = new Member10_3_1("member1", "멤버일", 11);
        Member10_3_1 member2 = new Member10_3_1("member2", "멤버이", 12);
        Member10_3_1 member3 = new Member10_3_1("member3", "멤버삼", 10);

        Member10_3_1 member4 = new Member10_3_1("member4", "멤버사", 13);
        Member10_3_1 member5 = new Member10_3_1("member5", "멤버오", 12);
        Member10_3_1 member6 = new Member10_3_1("member6", "멤버육", 11);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        em.persist(member5);
        em.persist(member6);

        Team10_3_1 team = new Team10_3_1("team1", "팀A");
        em.persist(team);
        Team10_3_1 team2 = new Team10_3_1("team2", "팀B");
        em.persist(team2);
        Team10_3_1 team3 = new Team10_3_1("team3", "팀C");
        em.persist(team3);

        member1.setTeam(team);
        member2.setTeam(team2);
        member3.setTeam(team3);

        member4.setTeam(team);
        member5.setTeam(team2);
        member6.setTeam(team3);

    }


}
