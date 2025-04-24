package Test;

import Dao.HoaDonDao;
import Dao.KhachHangDao;
import Model.HoaDon;
import Model.NhanVien;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import Enum.TrangThaiHoaDon;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class TestCRUDHoaDon {
    public static void main(String[] args) {

//        EntityManager em = Persistence.createEntityManagerFactory("default")
//                .createEntityManager();
//
//        EntityTransaction tr = em.getTransaction();
//
//        Faker faker = new Faker(new Locale("vi"));
//
//        HoaDonDao hoaDonDao = new HoaDonDao(em);
//
//        NhanVien nhanVien = new NhanVien();
//        nhanVien = null;
//
//        HoaDon hoaDon = new HoaDon();
//        hoaDon.setMaHoaDon(faker.idNumber().valid());
//        hoaDon.setNgayLapHoaDon(LocalDate.now().minusDays(ThreadLocalRandom.current().nextInt(1, 30)));
//        hoaDon.setSoLuongKhachHangNguoiLon(ThreadLocalRandom.current().nextInt(1, 5));
//        hoaDon.setSoLuongKhachHangTreEm(ThreadLocalRandom.current().nextInt(0, 3));
//        hoaDon.setTenNguoiMua(faker.name().fullName());
//        hoaDon.setSoDienThoaiNguoiMua("0" + faker.number().digits(9));
//        hoaDon.setThanhTien(faker.number().randomDouble(2, 100000, 1000000));
//        hoaDon.setTongTien(hoaDon.getThanhTien() + faker.number().randomDouble(2, 50, 500));
//        hoaDon.setNgayChinhSuaGanNhat(hoaDon.getNgayLapHoaDon().plusDays(ThreadLocalRandom.current().nextInt(1, 10)));
//        hoaDon.setTrangThai(faker.options().option(TrangThaiHoaDon.values()));
//        hoaDon.setNhanVien(nhanVien);
//
//
//        // Test chức năng thêm hóa đơn
//        System.out.println("\n Test chức năng thêm hóa đơn \n");
//        if (hoaDonDao.themHoaDon(hoaDon)) {
//            System.out.println("Thêm hóa đơn thành công!");
//        } else {
//            System.out.println("Thêm hóa đơn thất bại!");
//        }
//
//        // Test chức năng cập nhật thông tin hóa đơn
//        System.out.println("\n Test chức năng cập nhật thông tin hóa đơn \n");
//        hoaDon.setTrangThai(TrangThaiHoaDon.KHONGSUDUNG);
//        if (hoaDonDao.capNhatHoaDon(hoaDon)) {
//            System.out.println("Cập nhật hóa đơn thành công!");
//        } else {
//            System.out.println("Cập nhật hóa đơn thất bại!");
//        }
//
//        // Test chức năng tìm kiếm theo mãhóa đơn
//        System.out.println("\n Test chức năng tìm kiếm theo mã hóa đơn \n");
//        HoaDon hoaDonCanTim = hoaDonDao.timKiemHoadonTheoma(hoaDon.getMaHoaDon());
//        if (hoaDonCanTim != null) {
//            System.out.println(hoaDonCanTim);
//        } else {
//            System.out.println("hóa đơn không tồn tại!");
//        }
    }
}
