package Test;

import Model.Ve;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class TestCRUDVe {

//    public static void main(String[] args) {
//

    public static void main(String[] args) {

//        EntityManager em = Persistence.createEntityManagerFactory("default")
//                .createEntityManager();
//
//        EntityTransaction tr = em.getTransaction();
//
//        Faker faker = new Faker(new Locale("vi"));
//
//        VeDao veDao = new VeDao(em);
//
//        Ve ve = new Ve();
//        ve.setMaVe(faker.idNumber().valid());
//        ve.setNgayKhoiHanh(LocalDate.now().plusDays(ThreadLocalRandom.current().nextInt(1, 30)));
//        ve.setNgayDatVe(LocalDate.now().minusDays(ThreadLocalRandom.current().nextInt(1, 30)));
//        ve.setNhaGaKhoiHanh(faker.address().cityName());
//        ve.setNhaGaKetThuc(faker.address().cityName());
//        ve.setLoaiVe(faker.options().option("Thường", "VIP", "Gia đình"));
//        ve.setGiaVe(faker.number().randomDouble(2, 100000, 1000000));
//        ve.setLoaiGhe(faker.options().option("Ghế cứng", "Ghế mềm", "Giường nằm"));
//        ve.setViTriGhe((char) ('A' + ThreadLocalRandom.current().nextInt(0, 26)) + String.valueOf(ThreadLocalRandom.current().nextInt(1, 51)));
//        ve.setTenToa("Toa " + ThreadLocalRandom.current().nextInt(1, 10));
//        ve.setSoHieuTau("Tau " + ThreadLocalRandom.current().nextInt(100, 999));
//        ve.setMoTa("");
//
//        // Test chức năng thêm vé
//        System.out.println("\n Test chức năng thêm vé \n");
//        if (veDao.themVe(ve)) {
//            System.out.println("Thêm vé thành công!");
//        } else {
//            System.out.println("Thêm vé thất bại!");
//        }
//
//        // Test chức năng cập nhật thông tin vé
//        System.out.println("\n Test chức năng cập nhật thông tin vé \n");
//        ve.setGiaVe(550000);
//        if (veDao.capNhatVe(ve)) {
//            System.out.println("Cập nhật vé thành công!");
//        } else {
//            System.out.println("Cập nhật vé thất bại!");
//        }
//
//        // Test chức năng tìm kiếm theo mã vé
//        System.out.println("\n Test chức năng tìm kiếm theo mã vé \n");
//        Ve veCanTim = veDao.timKiemVeTheoMa(ve.getMaVe());
//        if (veCanTim != null) {
//            System.out.println(veCanTim);
//        } else {
//            System.out.println("Vé không tồn tại!");
//        }

//    }

    }

}
