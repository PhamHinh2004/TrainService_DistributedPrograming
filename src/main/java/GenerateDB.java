import Model.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import  Enum.TrangThaiHoaDon;
import  Enum.GioiTinh;



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
                nhanVien.setGioiTinh(faker.options().option("Nam", "Nữ"));
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

                // Tao HoaDon cho NhanVien
                HoaDon hoaDon = new HoaDon();
                hoaDon.setMaHoaDon(faker.idNumber().valid());
                hoaDon.setNgayLapHoaDon(LocalDate.now().minusDays(ThreadLocalRandom.current().nextInt(1, 30)));
                hoaDon.setSoLuongKhachHangNguoiLon(ThreadLocalRandom.current().nextInt(1, 5));
                hoaDon.setSoLuongKhachHangTreEm(ThreadLocalRandom.current().nextInt(0, 3));
                hoaDon.setTenNguoiMua(faker.name().fullName());
                hoaDon.setSoDienThoaiNguoiMua("0" + faker.number().digits(9));
                hoaDon.setThanhTien(faker.number().randomDouble(2, 100000, 1000000));
                hoaDon.setTongTien(hoaDon.getThanhTien() + faker.number().randomDouble(2, 50, 500));
                hoaDon.setNgayChinhSuaGanNhat(hoaDon.getNgayLapHoaDon().plusDays(ThreadLocalRandom.current().nextInt(1, 10)));
                hoaDon.setTrangThai(faker.options().option(TrangThaiHoaDon.values()));
                hoaDon.setNhanVien(nhanVien);

                // Tao KhachHang
                KhachHang khachHang = new KhachHang();
                khachHang.setMaKhachHang(faker.idNumber().valid());
                khachHang.setTenKhachHang(faker.name().fullName());
                khachHang.setSoDienThoai("0" + faker.number().digits(9));
                khachHang.setEmail(faker.internet().emailAddress());
                khachHang.setGioiTinh(faker.options().option(GioiTinh.values()));
                khachHang.setCCCD(String.format("%012d", faker.number().numberBetween(100000000000L, 999999999999L)));

                // Tạo dữ liệu giả cho vé
                Ve ve = new Ve();
                ve.setMaVe(faker.idNumber().valid());
                ve.setNgayKhoiHanh(LocalDate.now().plusDays(ThreadLocalRandom.current().nextInt(1, 30)));
                ve.setNgayDatVe(LocalDate.now().minusDays(ThreadLocalRandom.current().nextInt(1, 30)));
                ve.setNhaGaKhoiHanh(faker.address().cityName());
                ve.setNhaGaKetThuc(faker.address().cityName());
                ve.setLoaiVe(faker.options().option("Thường", "VIP", "Gia đình"));
                ve.setGiaVe(faker.number().randomDouble(2, 100000, 1000000));
                ve.setLoaiGhe(faker.options().option("Ghế cứng", "Ghế mềm", "Giường nằm"));
                ve.setViTriGhe((char) ('A' + ThreadLocalRandom.current().nextInt(0, 26)) + String.valueOf(ThreadLocalRandom.current().nextInt(1, 51)));
                ve.setTenToa("Toa " + ThreadLocalRandom.current().nextInt(1, 10));
                ve.setSoHieuTau("Tau " + ThreadLocalRandom.current().nextInt(100, 999));
                ve.setMoTa("");

                // Tạo ChiTietHoaDon cho mỗi HoaDon
                ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
                chiTietHoaDon.setHoaDon(hoaDon);
                chiTietHoaDon.setKhachHang(khachHang);
                chiTietHoaDon.setVe(ve);
                chiTietHoaDon.setGiaVe(ve.getGiaVe());
                chiTietHoaDon.setNgayTaoChiTietHoaDon(hoaDon.getNgayLapHoaDon());
                chiTietHoaDon.setNgayChinhSuaGanNhat(null);
                chiTietHoaDon.setThueVAT(hoaDon.getTongTien() * 0.1);  // Example VAT



                // Persist các đối tượng vào cơ sở dữ liệu
                em.persist(nhanVien);
                em.persist(taiKhoan);
                em.persist(trang);
                em.persist(quyen);
                em.persist(caTruc);
                em.persist(hoaDon);
                em.persist(khachHang);
                em.persist(ve);
                em.persist(chiTietHoaDon);
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
