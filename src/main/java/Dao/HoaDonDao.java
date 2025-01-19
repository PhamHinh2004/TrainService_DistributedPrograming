package Dao;

import Model.HoaDon;
import Model.KhachHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class HoaDonDao {
    private EntityManager em;

    public HoaDonDao(EntityManager em) {
        this.em = em;
    }

    public boolean themHoaDon(HoaDon hoaDon) {
        EntityTransaction tr = em.getTransaction();

        try{
            tr.begin();
            em.persist(hoaDon);
            tr.commit();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            tr.rollback();
        }

        return false;
    }

    public boolean capNhatHoaDon(HoaDon hoaDon) {
        EntityTransaction tr = em.getTransaction();

        try{
            tr.begin();
            em.merge(hoaDon);
            tr.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
        }

        return false;
    }

    public HoaDon timKiemHoadonTheoma(String maHoaDon) {
        return em.find(HoaDon.class, maHoaDon);
    }
}
