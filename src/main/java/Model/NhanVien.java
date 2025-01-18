package Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import Enum.GioiTinh;

@Data
@Entity
@Table(name = "nhanvien")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class NhanVien {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "nhanvien_id")
    private String maNhanVien;

    @Column(name = "tenNhanVien", length = 50)
    private String tenNhanVien;

    @Column(name = "ngaySinh", columnDefinition = "date")
    private String ngaySinh;

    @Enumerated(EnumType.STRING)
    private GioiTinh gioiTinh;

    @Column(name ="soDienThoai", length = 20)
    private String soDienThoai;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "CCCD", length = 12)
    private String CCCD;

    @OneToMany(mappedBy = "nhanvien", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CaTruc> catruc;

    @OneToOne(mappedBy = "nhanvien", cascade = CascadeType.ALL)
    private TaiKhoan taikhoan;

}