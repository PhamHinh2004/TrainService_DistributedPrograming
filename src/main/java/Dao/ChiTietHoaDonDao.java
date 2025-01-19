package Dao;

import Model.ChiTietHoaDon;
import Model.KhachHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class ChiTietHoaDonDao {
    private EntityManager em;

    public ChiTietHoaDonDao(EntityManager em) {
        this.em = em;
    }

    public boolean themChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
        EntityTransaction tr = em.getTransaction();

        try{
            tr.begin();
            em.persist(chiTietHoaDon);
            tr.commit();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            tr.rollback();
        }

        return false;
    }

    public boolean capNhatChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
        EntityTransaction tr = em.getTransaction();

        try{
            tr.begin();
            em.merge(chiTietHoaDon);
            tr.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
        }

        return false;
    }

    public List<ChiTietHoaDon> timKiemTheoMaHoaDon(String maHoaDon) {

        String query = "SELECT c FROM ChiTietHoaDon c WHERE c.hoaDon.maHoaDon = :maHoaDon";
        return em.createQuery(query, ChiTietHoaDon.class)
                .setParameter("maHoaDon", maHoaDon)
                .getResultList();
    }
}
