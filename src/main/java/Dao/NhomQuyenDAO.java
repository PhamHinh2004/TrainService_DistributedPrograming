package Dao;

import Model.NhomQuyen;
import jakarta.persistence.EntityManager;

public class NhomQuyenDAO extends GenericDAO<NhomQuyen, String> {
    public NhomQuyenDAO(Class<NhomQuyen> clazz) {
        super(clazz);
    }

    public NhomQuyenDAO(EntityManager em, Class<NhomQuyen> clazz) {
        super(em, clazz);
    }
}
