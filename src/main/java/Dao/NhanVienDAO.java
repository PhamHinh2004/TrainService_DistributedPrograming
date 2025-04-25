package Dao;

import Model.NhanVien;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import Enum.TrangThaiNhanVien;

public class NhanVienDAO extends GenericDAO<NhanVien, String>{
    public NhanVienDAO(Class<NhanVien> clazz) {
        super(clazz);
    }

    public NhanVienDAO(EntityManager em, Class<NhanVien> clazz) {
        super(em, clazz);
    }

    public boolean xoaNhanVien(String id) {
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();

            NhanVien nhanVien = em.find(NhanVien.class, id);

            nhanVien.setTrangThai(TrangThaiNhanVien.NGHILAM);

            em.merge(nhanVien);

            tr.commit();

            return true; // Cập nhật thành công

        } catch (Exception ex) {
            System.err.println("Lỗi khi cập nhật trạng thái nghỉ làm cho nhân viên ID " + id + ": " + ex.getMessage());
            ex.printStackTrace(); // In stack trace để debug
            if (tr.isActive()) {
                tr.rollback(); // Rollback nếu có lỗi
            }
            return false; // Trả về false do có lỗi
        }
    }


}
