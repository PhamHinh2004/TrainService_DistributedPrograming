package Excute;

import Model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import Enum.TrangThaiHoaDon;
import Enum.GioiTinh;
import java.time.LocalDate;

public class Truc_ChayChuongTrinh {

    public static void main(String[] args) {

//    public static void main(String[] args) {

//        HoaDon hoaDon1 = new HoaDon();
//        hoaDon1.setMaHoaDon("HD001");
//        hoaDon1.setNgayLapHoaDon(LocalDate.of(2025, 1, 16));
//        hoaDon1.setSoLuongKhachHangNguoiLon(2);
//        hoaDon1.setSoLuongKhachHangTreEm(1);
//        hoaDon1.setTenNguoiMua("Nguyen Van A");
//        hoaDon1.setSoDienThoaiNguoiMua("0123456789");
//        hoaDon1.setThanhTien(500000);
//        hoaDon1.setTongTien(550000);
//        hoaDon1.setNgayChinhSuaGanNhat(LocalDate.of(2025, 1, 17));
//        hoaDon1.setTrangThai(TrangThaiHoaDon.SUDUNG);
//
//
//        KhachHang khachHang1 = new KhachHang();
//        khachHang1.setMaKhachHang("KH001");
//        khachHang1.setTenKhachHang("Nguyen Thi B");
//        khachHang1.setSoDienThoai("0987654321");
//        khachHang1.setEmail("nguyenthitha@gmail.com");
//        khachHang1.setGioiTinh(GioiTinh.NU);
//        khachHang1.setCCCD("123456789");
//
//
//        Ve ve1 = new Ve();
//        ve1.setMaVe("V001");
//        ve1.setNgayKhoiHanh(LocalDate.of(2025, 2, 1));
//        ve1.setNgayDatVe(LocalDate.of(2025, 1, 15));
//        ve1.setNhaGaKhoiHanh("Ga Hà Nội");
//        ve1.setNhaGaKetThuc("Ga Sài Gòn");
//        ve1.setLoaiVe("Giường nằm");
//        ve1.setGiaVe(300000);
//        ve1.setLoaiGhe("Giường nằm");
//        ve1.setViTriGhe("A1");
//        ve1.setTenToa("Toa 1");
//        ve1.setSoHieuTau("SE1");
//        ve1.setMoTa("Vé giường nằm hạng VIP");
//
//
//        ChiTietHoaDon chiTietHoaDon1 = new ChiTietHoaDon();
//        chiTietHoaDon1.setHoaDon(hoaDon1);
//        chiTietHoaDon1.setKhachHang(khachHang1);
//        chiTietHoaDon1.setVe(ve1);
//        chiTietHoaDon1.setGiaVe(300000);
//        chiTietHoaDon1.setNgayTaoChiTietHoaDon(LocalDate.of(2025, 1, 16));
//        chiTietHoaDon1.setNgayChinhSuaGanNhat(LocalDate.of(2025, 1, 17));
//        chiTietHoaDon1.setThueVAT(10);
//
//
//        EntityManager em = Persistence.createEntityManagerFactory("default")
//                .createEntityManager();
//        EntityTransaction tr = em.getTransaction();
//
//        try {
//            tr.begin();
//            em.persist(hoaDon1);
//            em.persist(khachHang1);
//            em.persist(ve1);
//            em.persist(chiTietHoaDon1);
//
//            tr.commit();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            tr.rollback();
//        }
//
//        em.close();
    }

}
