package Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CaTruc {
    @Id
    @EqualsAndHashCode.Include
    private String maCaTruc;

    private LocalDateTime ngayGioBatDau;
    private LocalDateTime ngayGioKetThuc;
    private int tongHoaDon;
    private double tongTienCaTruoc;
    private double tongKetHoaDon;
    private double tongKetThucThu;
    private double thatThoat;


    // ==========================================
    @ManyToOne
    @JoinColumn(name = "maNhanVien")
    private NhanVien nhanVien;

}

