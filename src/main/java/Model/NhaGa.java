package Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NhaGa {
    @Id
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
