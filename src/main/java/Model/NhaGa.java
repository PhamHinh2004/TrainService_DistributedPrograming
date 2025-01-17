package Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToMany(mappedBy = "nhaGa")
    @ToString.Exclude
    Set<LichCapBenGa> lichCapBenGas;
}
