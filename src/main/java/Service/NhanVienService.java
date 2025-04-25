package Service;

import Model.NhanVien;

import java.rmi.RemoteException;

public interface NhanVienService extends GenericService<NhanVien, String>{
    boolean xoaNhanVien(String id) throws RemoteException;
}
