package Dao;

import Model.LichTrinh;
import Model.NhaGa;
import jakarta.persistence.EntityManager;

import java.util.List;

public class NhaGaDAO extends GenericDAO<NhaGa, String>{
    public NhaGaDAO(Class<NhaGa> clazz) {
        super(clazz);
    }

    public NhaGaDAO(EntityManager em, Class<NhaGa> clazz) {
        super(em, clazz);
    }

    public NhaGa findBySTTNhaGa(int stt) {
        List<NhaGa> nhaGaList = getAll();
        for(NhaGa nhaGa : nhaGaList){
            if(nhaGa.getSoThuTuNhaGa() == stt)
                return nhaGa;
        }

        return null;
    }

}
