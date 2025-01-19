package Dao;

import Model.KhachHang;
import Model.Ve;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class VeDao {
    private EntityManager em;

    public VeDao(EntityManager em) {
        this.em = em;
    }

    public boolean themVe(Ve ve) {
        EntityTransaction tr = em.getTransaction();

        try{
            tr.begin();
            em.persist(ve);
            tr.commit();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            tr.rollback();
        }

        return false;
    }

    public boolean capNhatVe(Ve ve) {
        EntityTransaction tr = em.getTransaction();

        try{
            tr.begin();
            em.merge(ve);
            tr.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
        }

        return false;
    }

    public Ve timKiemVeTheoMa(String maVe) {
        return em.find(Ve.class, maVe);
    }
}
