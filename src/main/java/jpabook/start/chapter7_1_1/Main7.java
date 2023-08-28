package jpabook.start.chapter7_1_1;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main7 {

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
        Book book = new Book();
        book.setName("부우욱");
        book.setPrice(10000);
        book.setAuthor("배유연");
        book.setIsbn("Isbn");
        em.persist(book);
        Book findBook = em.find(Book.class, book.getId());
        System.out.println("findBook = " + findBook);
    }

}
