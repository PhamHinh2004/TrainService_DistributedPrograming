package Dao;

import Model.Ve;
import jakarta.persistence.EntityManager;

public class VeDAO extends GenericDAO<Ve, String>{
    public VeDAO(Class<Ve> clazz) {
        super(clazz);
    }

    public VeDAO(EntityManager em, Class<Ve> clazz) {
        super(em, clazz);
    }
}
