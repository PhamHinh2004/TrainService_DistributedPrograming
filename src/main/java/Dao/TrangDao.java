package Dao;
import jakarta.persistence.*;
import Model.Trang;
import java.util.List;

public class TrangDao {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public void themTrang(Trang trang) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(trang);
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

    public void capNhatTrang(Trang trang) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(trang);
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

    public void xoaTrang(String maSoTrang) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Trang trang = em.find(Trang.class, maSoTrang);
            if (trang != null) {
                em.remove(trang);
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

    public Trang timTheoMaID(String maSoTrang) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Trang.class, maSoTrang);
        } finally {
            em.close();
        }
    }

    public List<Trang> dsCacTrang() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT t FROM Trang t", Trang.class).getResultList();
        } finally {
            em.close();
        }
    }
}
