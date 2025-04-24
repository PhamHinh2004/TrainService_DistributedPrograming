package Dao;

import Model.LichTrinh;
import jakarta.persistence.EntityManager;

public class LichTrinhDAO extends GenericDAO<LichTrinh, String> {
    public LichTrinhDAO(Class<LichTrinh> clazz) {
        super(clazz);
    }

    public LichTrinhDAO(EntityManager em, Class<LichTrinh> clazz) {
        super(em, clazz);
    }
}
