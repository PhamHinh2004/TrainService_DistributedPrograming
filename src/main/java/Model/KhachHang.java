package Model;

import jakarta.persistence.*;
import lombok.*;
import Enum.GioiTinh;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class KhachHang {
    @Id
    @EqualsAndHashCode.Include
    private String maKhachHang;

    private String tenKhachHang;
    private String soDienThoai;
    private String email;

    @Enumerated(EnumType.STRING)
    private GioiTinh gioiTinh;

    private String CCCD;

    // =====================
    @OneToMany(mappedBy = "khachHang")
    private Set<Ve> dsVe;
    // This constructor matches the signature you want to call
    public KhachHang(String maKhachHang, String tenKhachHang, String soDienThoai, String email, GioiTinh gioiTinh, String CCCD) {
        this.maKhachHang = maKhachHang;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.gioiTinh = gioiTinh;
        this.CCCD = CCCD;
        // You must initialize the dsVe collection, as it's not provided in this constructor
        this.dsVe = new HashSet<>(); // Initialize the Set to an empty HashSet
    }
}
