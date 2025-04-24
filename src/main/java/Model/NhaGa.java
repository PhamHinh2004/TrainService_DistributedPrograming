package Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NhaGa {
    @Id
    @EqualsAndHashCode.Include
    private String maNhaGa;

    private String tenNhaGa;
    private String diaChi;
    private String thanhPho;
    private String soDienThoai;
    private String email;
    private String diaChiWebSite;
    private int SoThuTuNhaGa;

    // ================================
    @OneToMany(mappedBy = "nhaGa")
    private Set<LichTrinh> dsLichTrinh;
}
