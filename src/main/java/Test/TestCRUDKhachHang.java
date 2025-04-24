package Test;

import Dao.KhachHangDao;
import Model.KhachHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import Enum.GioiTinh;
import net.datafaker.Faker;

import java.util.Locale;

public class TestCRUDKhachHang {
    public static void main(String[] args) {

        EntityManager em = Persistence.createEntityManagerFactory("default")
                .createEntityManager();

        EntityTransaction tr = em.getTransaction();

        Faker faker = new Faker(new Locale("vi"));

        KhachHangDao khachHangDao = new KhachHangDao(em);

        KhachHang khachHang = new KhachHang("KH001", "Nguyễn Văn A", "0900000001", "a.nguyen@example.com", GioiTinh.NAM, "111111111111");
        khachHang.setMaKhachHang(faker.idNumber().valid());
        khachHang.setTenKhachHang(faker.name().fullName());
        khachHang.setSoDienThoai("0" + faker.number().digits(9));
        khachHang.setEmail(faker.internet().emailAddress());
        khachHang.setGioiTinh(faker.options().option(GioiTinh.values()));
        khachHang.setCCCD(String.format("%012d", faker.number().numberBetween(100000000000L, 999999999999L)));

        // Test chức năng thêm khách hàng
        System.out.println("\n Test chức năng thêm khách hàng \n");
        if (khachHangDao.themKhachHang(khachHang)) {
            System.out.println("Thêm khách hàng thành công!");
        } else {
            System.out.println("Thêm khách hàng thất bại!");
        }

        // Test chức năng cập nhật thông tin khách hàng
        System.out.println("\n Test chức năng cập nhật thông tin khách hàng \n");
        khachHang.setSoDienThoai("0901234666");
        if (khachHangDao.capNhatKhachHang(khachHang)) {
            System.out.println("Cập nhật khách hàng thành công!");
        } else {
            System.out.println("Cập nhật khách hàng thất bại!");
        }

        // Test chức năng tìm kiếm theo mã khách hàng
        System.out.println("\n Test chức năng tìm kiếm theo mã khách hàng \n");
        KhachHang khachHangCanTim = khachHangDao.timKiemKhachHangTheoMa(khachHang.getMaKhachHang());
        if (khachHangCanTim != null) {
            System.out.println(khachHang);
        } else {
            System.out.println("Khách hàng không tồn tại!");
        }

        // Test chức năng tìm kiếm theo số điện thoại
        System.out.println("\n Test chức năng tìm kiếm theo số điện thoại \n");
        KhachHang khachHangCanTim1 = khachHangDao.timKiemKhachHangTheoSDT(khachHang.getSoDienThoai());
        if (khachHangCanTim1 != null) {
            System.out.println(khachHangCanTim1);
        } else {
            System.out.println("Khách hàng không tồn tại!");
        }


    }
}
