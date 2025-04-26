package Service;

import Model.NhaGa;

public interface NhaGaService extends GenericService<NhaGa, String>{
    NhaGa findBySTTNhaGa(int stt);
}
