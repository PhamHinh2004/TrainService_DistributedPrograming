package Test;

import Dao.HoaDonDao;
import Dao.KhachHangDao;
import Dao.VeDao;
import Model.HoaDon;
import Model.KhachHang;
import Model.Ve;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import Enum.TrangThaiHoaDon;
import  Enum.GioiTinh;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TestCRUDChiTietHoaDon {
//    public static void main(String[] args) {
//        EntityManager em = Persistence.createEntityManagerFactory("default")
//                .createEntityManager();
//
//        EntityTransaction tr = em.getTransaction();
//
//        Faker faker = new Faker(new Locale("vi"));
//
//        ChiTietHoaDonDao chiTietHoaDonDao = new ChiTietHoaDonDao(em);
//
//        HoaDon hoaDon = new HoaDon(faker.idNumber().valid(), LocalDate.now(), 1, 0, "Nguyen Van A", "0123456789", 1000.0, 1200.0, LocalDate.now(), TrangThaiHoaDon.SUDUNG, new ArrayList<>());
//        HoaDonDao hoaDonDao = new HoaDonDao(em);
//        hoaDonDao.themHoaDon(hoaDon);
//
//        KhachHang khachHang = new KhachHang(faker.idNumber().valid(), "Nguyen Thi B", "0987654321", "nguyenthib@example.com", GioiTinh.NU, "123456789012", null);
//        KhachHangDao khachHangDao = new KhachHangDao(em);
//        khachHangDao.themKhachHang(khachHang);
//
//        Ve ve = new Ve(faker.idNumber().valid(), LocalDate.now(), LocalDate.now(), "Ha Noi", "TP.HCM", "Thường", 500.0, "A1", "01", "Toa 1", "SE1", "", null);
//        VeDao veDao = new VeDao(em);
//        veDao.themVe(ve);
//
//        ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
//        chiTietHoaDon.setHoaDon(hoaDon);
//        chiTietHoaDon.setKhachHang(khachHang);
//        chiTietHoaDon.setVe(ve);
//        chiTietHoaDon.setGiaVe(500.0);
//        chiTietHoaDon.setNgayTaoChiTietHoaDon(LocalDate.now());
//        chiTietHoaDon.setNgayChinhSuaGanNhat(LocalDate.now());
//        chiTietHoaDon.setThueVAT(10.0);
//
//        // Test chức năng thêm chi tiết hóa đơn
//        System.out.println("\n Test chức năng thêm chi tiết hóa đơn \n");
//        if (chiTietHoaDonDao.themChiTietHoaDon(chiTietHoaDon)) {
//            System.out.println("Thêm chi tiết hóa đơn thành công!");
//        } else {
//            System.out.println("Thêm chi tiết hóa đơn thất bại!");
//        }
//
//        // Test chức năng cập nhật thông tin chi tiết hóa đơn
//        System.out.println("\n Test chức năng cập nhật thông tin chi tiết hóa đơn \n");
//        chiTietHoaDon.setNgayTaoChiTietHoaDon(LocalDate.of(2025, 2, 15));
//        if (chiTietHoaDonDao.capNhatChiTietHoaDon(chiTietHoaDon)) {
//            System.out.println("Cập nhật chi tiết hóa đơn thành công!");
//        } else {
//            System.out.println("Cập nhật chi tiết hóa đơn thất bại!");
//        }
//        // Test chức năng tìm kiếm theo mã hóa đơn
//        System.out.println("\n Test chức năng tìm kiếm theo mã hóa đơn \n");
//        List<ChiTietHoaDon> danhSachCTHD = chiTietHoaDonDao.timKiemTheoMaHoaDon(hoaDon.getMaHoaDon());
//        if (danhSachCTHD != null && !danhSachCTHD.isEmpty()) {
//            for (ChiTietHoaDon cthd : danhSachCTHD) {
//                System.out.println(cthd);
//            }
//        } else {
//            System.out.println("Không tìm thấy chi tiết hóa đơn với mã hóa đơn: HD0003");
//        }
//
//
//
//    }
}
