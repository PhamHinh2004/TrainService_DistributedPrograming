package Dao;

import Model.KhachHang;
import jakarta.persistence.EntityManager;

public class KhachHangDAO extends GenericDAO<KhachHang, String>{
    public KhachHangDAO(Class<KhachHang> clazz) {
        super(clazz);
    }

    public KhachHangDAO(EntityManager em, Class<KhachHang> clazz) {
        super(em, clazz);
    }
}
