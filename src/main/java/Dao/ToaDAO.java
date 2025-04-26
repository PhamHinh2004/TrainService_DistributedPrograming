package Dao;

import Model.Toa;
import jakarta.persistence.EntityManager;

public class ToaDAO extends GenericDAO<Toa, String>{
    public ToaDAO(Class<Toa> clazz) {
        super(clazz);
    }

    public ToaDAO(EntityManager em, Class<Toa> clazz) {
        super(em, clazz);
    }
}
