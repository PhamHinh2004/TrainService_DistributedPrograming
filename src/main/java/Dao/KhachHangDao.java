package Dao;

import Model.KhachHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class KhachHangDao {
    private EntityManager em;

    public KhachHangDao(EntityManager em) {
        this.em = em;
    }

    public boolean themKhachHang(KhachHang khachHang) {
        EntityTransaction tr = em.getTransaction();

        try{
            tr.begin();
            em.persist(khachHang);
            tr.commit();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            tr.rollback();
        }

        return false;
    }

    public boolean capNhatKhachHang(KhachHang khachHang) {
        EntityTransaction tr = em.getTransaction();

        try{
            tr.begin();
            em.merge(khachHang);
            tr.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
        }

        return false;
    }

    public KhachHang timKiemKhachHangTheoMa(String maKhachHang) {
        return em.find(KhachHang.class, maKhachHang);
    }

    public KhachHang timKiemKhachHangTheoSDT(String soDienThoai) {
        try {
            String query = "SELECT kh FROM KhachHang kh WHERE kh.soDienThoai = :soDienThoai";
            return em.createQuery(query, KhachHang.class)
                    .setParameter("soDienThoai", soDienThoai)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
