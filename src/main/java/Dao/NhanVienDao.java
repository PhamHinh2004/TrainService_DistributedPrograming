package Dao;

import Model.NhanVien;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class NhanVienDao {
    private EntityManager em;
    public NhanVienDao(EntityManager em) {
        this.em = em;
    }

    public boolean themNhanVien(NhanVien nv)
    {
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(nv);
            tr.commit();
            return true;
        } catch (Exception e) {
            tr.rollback();
            e.printStackTrace();
        }
        return false;
    }
    // Xoa nhan vien
    public boolean xoaNhanVien(String id)
    {
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            NhanVien nv = em.find(NhanVien.class, id);
            em.remove(nv);
            tr.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
        }
        return false;

    }
    // Sua nhan vien
    public boolean suaNhanVien(NhanVien nv)
    {
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.merge(nv);
            tr.commit();
            return true;
        } catch (Exception e) {
            tr.rollback();
            e.printStackTrace();
        }
        return false;
    }
    // Tim nhan vien theo id
    public NhanVien timNhanVien(String id)
    {
        return em.find(NhanVien.class, id);
    }
    public List<NhanVien> timTatCaNhanVien() {
        return em.createQuery("SELECT nv FROM NhanVien nv", NhanVien.class).getResultList();
    }

    // Tìm nhân viên theo tên
    public List<NhanVien> timNhanVienTheoTen(String ten) {
        return em.createQuery("SELECT nv FROM NhanVien nv WHERE nv.tenNhanVien LIKE :ten", NhanVien.class)
                .setParameter("ten", "%" + ten + "%")
                .getResultList();
    }
}
