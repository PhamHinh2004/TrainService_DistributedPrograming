package Dao;

import Model.Quyen;
import jakarta.persistence.EntityManager;

public class QuyenDAO extends GenericDAO<Quyen, String >{
    public QuyenDAO(Class<Quyen> clazz) {
        super(clazz);
    }

    public QuyenDAO(EntityManager em, Class<Quyen> clazz) {
        super(em, clazz);
    }
}
