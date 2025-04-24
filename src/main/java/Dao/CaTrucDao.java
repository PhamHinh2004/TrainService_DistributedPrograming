package Dao;

import Model.CaTruc;
import Model.LichTrinh;
import jakarta.persistence.EntityManager;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class CaTrucDao extends GenericDAO<CaTruc, String> {
    public CaTrucDao(Class<CaTruc> clazz) {
        super(clazz);
    }

    public CaTrucDao(EntityManager em, Class<CaTruc> clazz) {
        super(em, clazz);
    }

    public static void main(String[] args) {
        CaTrucDao caTrucDao = new CaTrucDao(CaTruc.class);

        // test find
//        CaTruc caTruc = caTrucDao.findById("CT005");
//        System.out.println(caTruc);

        // test save
        CaTruc caTrucNew = new CaTruc();

        caTrucNew.setMaCaTruc("CT005"); // Gán giá trị thủ công cho maCaTruc
        caTrucNew.setNgayGioBatDau(LocalDateTime.of(2025, 4, 27, 8, 0));
        caTrucNew.setNgayGioKetThuc(LocalDateTime.of(2025, 4, 27, 16, 0));
        caTrucNew.setTongHoaDon(12);
        caTrucNew.setTongTienCaTruoc(6000000.0);
        caTrucNew.setTongKetHoaDon(5800000.0);
        caTrucNew.setTongKetThucThu(5750000.0);
        caTrucNew.setThatThoat(30000.0);
//
//        if(caTrucDao.save(caTrucNew))
//            System.out.println(caTrucDao.findById("CT005"));
//        else System.out.println("Them khong thanh cong");

        // test update
//        caTrucNew.setTongHoaDon(100);
//        if (caTrucDao.update(caTrucNew))
//            System.out.println(caTrucDao.findById("CT005"));
//        else
//            System.out.println("cap nhat khong thanh cong");

        // test delete
//        if(caTrucDao.delete("CT005"))
//            System.out.println(caTrucDao.findById("CT005"));
//        else
//            System.out.println("Xoa khong thanh cong");

        // test getAll
        List<CaTruc> list = caTrucDao.getAll();
        list.forEach(System.out::println);
    }
}
