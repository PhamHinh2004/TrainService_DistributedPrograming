package Dao;

import Model.LichTrinh;
import jakarta.persistence.EntityManager;

public class NhaGaDAO extends GenericDAO<LichTrinh, String>{
    public NhaGaDAO(Class<LichTrinh> clazz) {
        super(clazz);
    }

    public NhaGaDAO(EntityManager em, Class<LichTrinh> clazz) {
        super(em, clazz);
    }
}
