package Dao;
import Model.NhomQuyen;
import jakarta.persistence.*;

import java.util.List;

public class TrangDao {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public void themTrang(NhomQuyen nhomQuyen) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(nhomQuyen);
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

    public void capNhatTrang(NhomQuyen nhomQuyen) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(nhomQuyen);
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
            NhomQuyen nhomQuyen = em.find(NhomQuyen.class, maSoTrang);
            if (nhomQuyen != null) {
                em.remove(nhomQuyen);
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

    public NhomQuyen timTheoMaID(String maSoTrang) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(NhomQuyen.class, maSoTrang);
        } finally {
            em.close();
        }
    }

    public List<NhomQuyen> dsCacTrang() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT t FROM NhomQuyen t", NhomQuyen.class).getResultList();
        } finally {
            em.close();
        }
    }
}
