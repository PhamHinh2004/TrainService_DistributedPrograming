package Dao;

import Model.HoaDon;
import Enum.TrangThaiHoaDon; // Import Enum TrangThaiHoaDon
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;

public class HoaDonDAO extends GenericDAO<HoaDon, String> {

    private EntityManager entityManager; // Khai báo EntityManager

    public HoaDonDAO(EntityManager em) {
        super(em, HoaDon.class);
        this.entityManager = em; // Gán EntityManager được truyền vào constructor
    }

    public List<HoaDon> getAllHoaDonWithDetails() {
        try {
            Query query = entityManager.createQuery("SELECT h FROM HoaDon h LEFT JOIN FETCH h.dsVe LEFT JOIN FETCH h.nhanVien ORDER BY h.ngayLapHoaDon DESC", HoaDon.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Lỗi khi tải dữ liệu hóa đơn với chi tiết: " + e.getMessage());
            return null; // Hoặc throw một exception tùy theo cách bạn xử lý lỗi
        }
    }

    // Bạn có thể thêm các phương thức khác để truy vấn HoaDon theo các tiêu chí khác
    // ví dụ:
    public HoaDon getHoaDonByMa(String maHoaDon) {
        try {
            return entityManager.find(HoaDon.class, maHoaDon);
        } catch (Exception e) {
            System.err.println("Lỗi khi tìm hóa đơn theo mã: " + e.getMessage());
            return null;
        }
    }

    // Ví dụ về phương thức tìm kiếm hóa đơn theo mã hoặc tên khách hàng (nếu cần)
    public List<HoaDon> searchHoaDon(String searchText) {
        try {
            Query query = entityManager.createQuery(
                    "SELECT h FROM HoaDon h LEFT JOIN FETCH h.nhanVien LEFT JOIN FETCH h.dsVe " +
                            "WHERE LOWER(h.maHoaDon) LIKE :searchText OR LOWER(h.tenNguoiMua) LIKE :searchText " +
                            "ORDER BY h.ngayLapHoaDon DESC", HoaDon.class);
            query.setParameter("searchText", "%" + searchText + "%");
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Lỗi khi tìm kiếm hóa đơn: " + e.getMessage());
            return null;
        }
    }

    // Ví dụ về phương thức lọc hóa đơn theo trạng thái (nếu cần)
    public List<HoaDon> filterHoaDonByStatus(String status) {
        try {
            Query query = entityManager.createQuery(
                    "SELECT h FROM HoaDon h LEFT JOIN FETCH h.nhanVien LEFT JOIN FETCH h.dsVe " +
                            "WHERE h.trangThai = :status " +
                            "ORDER BY h.ngayLapHoaDon DESC", HoaDon.class);
            query.setParameter("status", TrangThaiHoaDon.valueOf(status)); // Sử dụng Enum.TrangThaiHoaDon
            return query.getResultList();
        } catch (IllegalArgumentException e) {
            System.err.println("Trạng thái hóa đơn không hợp lệ: " + status + " - " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Lỗi khi lọc hóa đơn theo trạng thái: " + e.getMessage());
            return null;
        }
    }

    // Ví dụ về phương thức lọc hóa đơn theo trạng thái và tìm kiếm (kết hợp)
    public List<HoaDon> filterAndSearchHoaDon(String searchText, String status) {
        try {
            String jpql = "SELECT h FROM HoaDon h LEFT JOIN FETCH h.nhanVien LEFT JOIN FETCH h.dsVe WHERE 1=1 ";
            if (searchText != null && !searchText.isEmpty()) {
                jpql += "AND (LOWER(h.maHoaDon) LIKE :searchText OR LOWER(h.tenNguoiMua) LIKE :searchText) ";
            }
            if (status != null && !status.isEmpty()) {
                jpql += "AND h.trangThai = :status ";
            }
            jpql += "ORDER BY h.ngayLapHoaDon DESC";

            Query query = entityManager.createQuery(jpql, HoaDon.class);
            if (searchText != null && !searchText.isEmpty()) {
                query.setParameter("searchText", "%" + searchText + "%");
            }
            if (status != null && !status.isEmpty()) {
                query.setParameter("status", TrangThaiHoaDon.valueOf(status)); // Sử dụng Enum.TrangThaiHoaDon
            }
            return query.getResultList();
        } catch (IllegalArgumentException e) {
            System.err.println("Trạng thái hóa đơn không hợp lệ: " + status + " - " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Lỗi khi lọc và tìm kiếm hóa đơn: " + e.getMessage());
            return null;
        }
    }
    public void updateHoaDon(HoaDon hoaDon) {
        entityManager.getTransaction().begin();
        entityManager.merge(hoaDon); // Or entityManager.persist(hoaDon); depending on your needs
        entityManager.getTransaction().commit();
    }
}