package jpabook.start.chapter7_3_5;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class BoardMain {

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
        Board2 board2 = new Board2();
        board2.setTitle("제목22");
        em.persist(board2);

        BoardDetail boardDetail = new BoardDetail();
        boardDetail.setContent("내용내용");
        boardDetail.setBoard(board2);

        em.persist(boardDetail);

    }

}
