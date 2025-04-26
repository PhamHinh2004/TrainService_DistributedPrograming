package Dao;

import Model.Tau;
import jakarta.persistence.EntityManager;

public class TauDAO extends GenericDAO<Tau, String >{
    public TauDAO(Class<Tau> clazz) {
        super(clazz);
    }

    public TauDAO(EntityManager em, Class<Tau> clazz) {
        super(em, clazz);
    }
}
