package Dao;
import jakarta.persistence.*;
import Model.Quyen;
import java.util.List;

public class QuyenDao {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public void themQuyen(Quyen quyen) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(quyen);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void capNhatQuyen(Quyen quyen) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(quyen);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void xoaQuyen(String maQuyen) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Quyen quyen = em.find(Quyen.class, maQuyen);
            if (quyen != null) {
                em.remove(quyen);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public Quyen findById(String maQuyen) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Quyen.class, maQuyen);
        } finally {
            em.close();
        }
    }

    public List<Quyen> dsQuyen() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT q FROM Quyen q", Quyen.class).getResultList();
        } finally {
            em.close();
        }
    }
}
