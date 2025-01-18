import Model.CaTruc;
import Model.NhanVien;
import Model.Quyen;
import Model.TaiKhoan;
import Model.Trang;
import Enum.GioiTinh;
import net.datafaker.Faker;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import Model.CaTruc;
import Model.NhanVien;
import Model.Quyen;
import Model.TaiKhoan;
import Model.Trang;
import net.datafaker.Faker;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;
import java.util.Locale;

public class GenerateDB {
    public static void main(String[] args) {
        // Tạo EntityManager và EntityTransaction
        EntityManager em = Persistence.createEntityManagerFactory("default").createEntityManager();
        EntityTransaction tr = em.getTransaction();

        // Khởi tạo Faker để tạo dữ liệu giả
        Faker faker = new Faker(new Locale("vi"));

        try {
            tr.begin();

            // Tạo dữ liệu giả cho NhanVien và các đối tượng liên quan
            for (int i = 0; i < 10; i++) {  // Tạo 10 nhân viên giả
                // Tạo NhanVien
                NhanVien nhanVien = new NhanVien();
                nhanVien.setMaNhanVien(faker.idNumber().valid());
                nhanVien.setTenNhanVien(faker.name().fullName());
                GioiTinh[] gioiTinhs = GioiTinh.values();
                GioiTinh gioiTinh = gioiTinhs[new Random().nextInt(gioiTinhs.length)];
                nhanVien.setGioiTinh(gioiTinh);  // Set giá trị enum
                String soDienThoai = faker.phoneNumber().cellPhone();
                if (soDienThoai.length() > 11) {
                    soDienThoai = soDienThoai.substring(0, 11); // Cắt bớt nếu dài hơn 11 ký tự
                }
                nhanVien.setSoDienThoai(soDienThoai);
                nhanVien.setEmail(faker.internet().emailAddress());
                nhanVien.setCCCD(faker.idNumber().valid());

                // Tạo TaiKhoan cho NhanVien
                TaiKhoan taiKhoan = new TaiKhoan();
                taiKhoan.setMaTaiKhoan(faker.idNumber().valid());
                taiKhoan.setTenTaiKhoan(faker.name().username());
                taiKhoan.setMatKhau(faker.internet().password());
                taiKhoan.setVaiTro(faker.options().option("Admin", "User", "Manager"));
                taiKhoan.setNhanvien(nhanVien);

                // Tạo Trang cho TaiKhoan
                Trang trang = new Trang();
                trang.setMaSoTrang(faker.idNumber().valid());
                trang.setTenTrang(faker.options().option("Trang Chủ", "Trang Quản Lý Hóa Đơn", "Trang Quản Lý Nhân Viên"
                        ,"Trang Quản Lý Vé", "Trang Quản Lý Khách Hàng", "Trang Thống Kê"));
                trang.setTaikhoan(taiKhoan);

                // Tạo Quyen cho Trang
                Quyen quyen = new Quyen();
                quyen.setMaQuyen(faker.idNumber().valid());
                quyen.setTenQuyen(faker.options().option("Read", "Write", "Delete", "Update"));
                quyen.setTrang(trang);

                // Tạo CaTruc cho NhanVien
                CaTruc caTruc = new CaTruc();
                caTruc.setMaCaTruc(faker.idNumber().valid());
                caTruc.setNgayGioBatDau(LocalDateTime.now().minusDays(ThreadLocalRandom.current().nextInt(1, 10)));
                caTruc.setNgayGioKetThuc(caTruc.getNgayGioBatDau().plusHours(ThreadLocalRandom.current().nextInt(4, 10)));
                caTruc.setTongHoaDon(faker.number().numberBetween(10, 100));
                caTruc.setTongTienCaTruoc(faker.number().randomDouble(2, 1000, 5000));
                caTruc.setTongKetHoaDon(faker.number().randomDouble(2, 1000, 5000));
                caTruc.setTongKetThucThu(faker.number().randomDouble(2, 1000, 5000));
                caTruc.setThatThoat(faker.number().randomDouble(2, 100, 1000));
                caTruc.setTongVat(faker.number().randomDouble(2, 100, 1000));
                caTruc.setNhanvien(nhanVien);

                // Persist các đối tượng vào cơ sở dữ liệu
                em.persist(nhanVien);
                em.persist(taiKhoan);
                em.persist(trang);
                em.persist(quyen);
                em.persist(caTruc);
            }

            tr.commit();
            System.out.println("Dữ liệu giả đã được thêm vào cơ sở dữ liệu.");

        } catch (Exception e) {
            if (tr.isActive()) {
                tr.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
