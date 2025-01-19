import Dao.NhanVienDao;
import Model.NhanVien;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import net.datafaker.Faker;
import Enum.GioiTinh;

import javax.swing.text.html.parser.Entity;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TestCRUD {
    public static void main(String[] args) {
        EntityManager em = Persistence.createEntityManagerFactory("default")
                .createEntityManager();
        NhanVienDao nhanVienDao = new NhanVienDao(em);
        Faker faker = new Faker(new Locale("vi"));
        Random random = new Random();

        NhanVien nv = new NhanVien();
        // Demo thêm nhân viên
        String id = String.format("NV021");
        nv.setMaNhanVien(id);
        String tenNhanVien = "Duong The Khanh";
        nv.setTenNhanVien(tenNhanVien);
        int age = 20;
        String ngaySinh = "2004-04-11";
        nv.setNgaySinh(ngaySinh);

        GioiTinh gioiTinh = GioiTinh.Nam;
        nv.setGioiTinh(gioiTinh);
        String soDienThoai = "0901234567";
        nv.setSoDienThoai(soDienThoai);

        String email = Normalizer.normalize(tenNhanVien, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("\\s+", "")
                .toLowerCase() + "@gmail.com";
        nv.setEmail(email);
        String cccd = "124-76-1891";  // Nhập CCCD
        nv.setCCCD(cccd);
        boolean isAdded = nhanVienDao.themNhanVien(nv);
        System.out.println("Thêm nhân viên " + nv.getTenNhanVien() + ": " + (isAdded ? "Thành công" : "Thất bại"));

        // Demo tìm nhân viên theo id
        NhanVien timNV = nhanVienDao.timNhanVien("NV001");
        if (timNV != null) {
            System.out.println("Tìm thấy nhân viên: " + timNV.getTenNhanVien());
        } else {
            System.out.println("Không tìm thấy nhân viên.");
        }

        // Demo tìm tất cả nhân viên
        List<NhanVien> danhSachNhanVien = nhanVienDao.timTatCaNhanVien();
        System.out.println("Danh sách nhân viên:");
        for (NhanVien nhanVien : danhSachNhanVien) {
            System.out.println(nhanVien.getTenNhanVien());
        }

        // Demo xóa nhân viên
        boolean isDeleted = nhanVienDao.xoaNhanVien("NV001");
        System.out.println("Xóa nhân viên: " + (isDeleted ? "Thành công" : "Thất bại"));


        // Đóng EntityManager và EntityManagerFactory
        em.close();
        em.close();
    }
    }

