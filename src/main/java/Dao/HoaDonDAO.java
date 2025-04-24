package Dao;

import Model.HoaDon;
import jakarta.persistence.EntityManager;

public class HoaDonDAO extends GenericDAO<HoaDon, String>{
    public HoaDonDAO(Class<HoaDon> clazz) {
        super(clazz);
    }

    public HoaDonDAO(EntityManager em, Class<HoaDon> clazz) {
        super(em, clazz);
    }
}
