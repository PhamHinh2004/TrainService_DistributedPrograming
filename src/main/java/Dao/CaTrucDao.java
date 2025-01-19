package Dao;

import Model.CaTruc;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class CaTrucDao {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public void themCaTruc(CaTruc caTruc) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(caTruc);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public CaTruc timCaTrucTheoID(String maCaTruc) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(CaTruc.class, maCaTruc);
        } finally {
            em.close();
        }
    }

    public List<CaTruc> tatCaCaTruc() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM CaTruc c", CaTruc.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void capNhatCaTruc(CaTruc caTruc) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(caTruc);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void XoaCaTruc(String maCaTruc) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            CaTruc caTruc = em.find(CaTruc.class, maCaTruc);
            if (caTruc != null) {
                em.remove(caTruc);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    public List<CaTruc> findByNhanVienId(String nhanVienId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM CaTruc c WHERE c.nhanvien.maNhanVien = :nhanVienId", CaTruc.class)
                    .setParameter("nhanVienId", nhanVienId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
