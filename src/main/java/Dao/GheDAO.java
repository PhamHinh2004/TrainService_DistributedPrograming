package Dao;

import Model.Ghe;
import jakarta.persistence.EntityManager;

public class GheDAO extends GenericDAO<Ghe, String>{
    public GheDAO(Class<Ghe> clazz) {
        super(clazz);
    }

    public GheDAO(EntityManager em, Class<Ghe> clazz) {
        super(em, clazz);
    }
}
