package jpabook.start.chapter10_2_7;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main10_2_7 {

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
            //getMemberData(em);
            //innerJoinTest(em);
            //outerJoinTest(em);
            //setaJoin(em);
            //useJoinOn(em);
            //fetchJoin(em);
            fetchWrongUse(em);
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void getMemberData(EntityManager em) {

        String jpql = "select t from Team10_2_7 t join t.members where t.name='팀A'";

        List<Team10_2_7> resultList = em.createQuery(jpql, Team10_2_7.class).getResultList();
        resultList.stream().forEach(a -> System.out.println("team = " + a));
        System.out.println("---------------------------");
       /* List<Member10_2_7> resultMemberList = em.createQuery(jpql, Member10_2_7.class)
                .getResultList();
        System.out.println("아예안가져올꺼야...? :: " + resultMemberList.size());
        resultMemberList.stream().forEach(a -> System.out.println("member = " + a));*/

        //패치 조인
        String fetchJoin = "select t from Team10_2_7 t join fetch t.members where t.name='팀A'";
        List<Team10_2_7> resultListFetch = em.createQuery(fetchJoin, Team10_2_7.class)
                .getResultList();
        resultListFetch.stream().forEach(a -> System.out.println("team = " + a));

        List<Member10_2_7> resultMemberList = em.createQuery(fetchJoin, Member10_2_7.class)
                .getResultList();
        System.out.println("아예안가져올꺼야...? :: " + resultMemberList.size());
        resultMemberList.stream().forEach(a -> System.out.println("member = " + a));
    }

    private static void save(EntityManager em) {
        Member10_2_7 member1 = new Member10_2_7("member1", "멤버일");
        Member10_2_7 member2 = new Member10_2_7("member2", "멤버이");
        Member10_2_7 member3 = new Member10_2_7("member3", "멤버삼");

        Member10_2_7 member4 = new Member10_2_7("member4", "멤버사");
        Member10_2_7 member5 = new Member10_2_7("member5", "멤버오");
        Member10_2_7 member6 = new Member10_2_7("member6", "멤버육");

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        em.persist(member5);
        em.persist(member6);

        Team10_2_7 team = new Team10_2_7("team1", "팀A");
        em.persist(team);
        Team10_2_7 team2 = new Team10_2_7("team2", "팀B");
        em.persist(team2);
        Team10_2_7 team3 = new Team10_2_7("team3", "팀C");
        em.persist(team3);

        member1.setTeam(team);
        member2.setTeam(team2);
        member3.setTeam(team3);

        member4.setTeam(team);
        member5.setTeam(team2);
        member6.setTeam(team3);

    }

    private static void innerJoinTest(EntityManager em) {
        String teamName = "팀A";
        /*String query = "SELECT m FROM Member10_2_7 m INNER JOIN m.team t WHERE t.name = :teamName";

        List<Member10_2_7> members = em.createQuery(query, Member10_2_7.class)
                .setParameter("teamName", teamName)
                .getResultList();

        System.out.println("members = " + members);*/

        String query2 = "SELECT m FROM Member10_2_7 m inner join m.team t";

        List<Member10_2_7> members = em.createQuery(query2, Member10_2_7.class).getResultList();
        for (Member10_2_7 member : members) {
            Team10_2_7 team = member.getTeam();
            String name = team.getName();
            System.out.println("name = " + name);
        }

        /*String query3 = "SELECT t FROM Team10_2_7 t inner join t.members m";

        List<Team10_2_7> teams = em.createQuery(query3, Team10_2_7.class)
                .getResultList();
        for (Team10_2_7 team : teams) {
            List<Member10_2_7> members = team.getMembers();
            System.out.println("members = " + members);
        }*/

        /*for (Member10_2_7 member : members) {
            Team10_2_7 team = member.getTeam();
            String name = team.getName();
            System.out.println("name = " + name);
        }*/
    }

    private static void outerJoinTest(EntityManager em) {
        String query = "SELECT m FROM Member10_2_7 m LEFT OUTER JOIN m.team t";

        List<Member10_2_7> members = em.createQuery(query, Member10_2_7.class)
                .getResultList();

        System.out.println("members = " + members);
    }

    private static void setaJoin(EntityManager em) {
        String query = "SELECT m FROM Member10_2_7 m join m.team t where m.username=t.name";

        List<Member10_2_7> members = em.createQuery(query, Member10_2_7.class)
                .getResultList();

        System.out.println("members = " + members);
    }

    private static void useJoinOn(EntityManager em) {
        String query = "SELECT m FROM Member10_2_7 m left join m.team t on t.name='팀A'";

        List<Member10_2_7> members = em.createQuery(query, Member10_2_7.class)
                .getResultList();

        System.out.println("members = " + members);
    }

    private static void fetchJoin(EntityManager em) {
        /*String query = "select m from Member10_2_7 m join fetch m.team t where t.name='팀B'";

        List<Member10_2_7> members = em.createQuery(query, Member10_2_7.class)
                .getResultList();

        System.out.println("members = " + members);*/
        String query = "select t from Team10_2_7 t join fetch t.members m where m.username='멤버일'";

        List<Team10_2_7> teams = em.createQuery(query, Team10_2_7.class).getResultList();
        for (Team10_2_7 team : teams) {
            System.out.println("team.getMembers() = " + team.getMembers());
        }

        //System.out.println("teams = " + teams);
    }

    private static void fetchWrongUse(EntityManager em) {

        List<Team10_2_7> teams =
                em.createQuery("select distinct t from Team10_2_7 t join fetch t.members m "
                                        + "where m.username='멤버일'",
                                Team10_2_7.class)
                        .getResultList(); //넌 사용하지 않을테다.

        List<Team10_2_7> teams2 =
                em.createQuery("select distinct t from Team10_2_7 t join fetch t.members m "
                                , Team10_2_7.class)
                        .getResultList();

        for (Team10_2_7 team : teams2) {
            System.out.println("team.getMembers() = " + team.getMembers());
        }
    }

}
