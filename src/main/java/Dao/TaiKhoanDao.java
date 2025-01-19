package Dao;

import Model.TaiKhoan;
import jakarta.persistence.*;

import java.util.List;

public class TaiKhoanDao {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("your_persistence_unit_name");

    // Lưu tài khoản mới
    public void themTaiKhoan(TaiKhoan taiKhoan) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(taiKhoan);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Tìm tài khoản theo mã
    public TaiKhoan timTaiKhoanBangMa(String maTaiKhoan) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(TaiKhoan.class, maTaiKhoan);
        } finally {
            em.close();
        }
    }

    // Lấy danh sách tất cả tài khoản
    public List<TaiKhoan> dsTaiKhoan() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT t FROM TaiKhoan t", TaiKhoan.class).getResultList();
        } finally {
            em.close();
        }
    }

    // Cập nhật thông tin tài khoản
    public void capNhatTaiKhoan(TaiKhoan taiKhoan) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(taiKhoan);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Xóa tài khoản theo mã
    public void xoaTaiKhoan(String maTaiKhoan) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            TaiKhoan taiKhoan = em.find(TaiKhoan.class, maTaiKhoan);
            if (taiKhoan != null) {
                em.remove(taiKhoan);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public TaiKhoan findByMaNhanVien(String maNhanVien) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT t FROM TaiKhoan t WHERE t.nhanvien.maNhanVien = :maNhanVien", TaiKhoan.class)
                    .setParameter("maNhanVien", maNhanVien)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Không tìm thấy tài khoản
        } finally {
            em.close();
        }
    }
}
