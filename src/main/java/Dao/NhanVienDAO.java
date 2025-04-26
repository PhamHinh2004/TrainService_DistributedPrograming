package Dao;

import Model.NhanVien;
import jakarta.persistence.EntityManager;

public class NhanVienDAO extends GenericDAO<NhanVien, String>{
    public NhanVienDAO(Class<NhanVien> clazz) {
        super(clazz);
    }

    public NhanVienDAO(EntityManager em, Class<NhanVien> clazz) {
        super(em, clazz);
    }
}
