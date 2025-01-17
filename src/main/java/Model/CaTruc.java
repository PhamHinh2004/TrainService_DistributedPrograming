package Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "catruc")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CaTruc {
    @Id
    @Column(name = "catruc_id" )
    @EqualsAndHashCode.Include
    private String maCaTruc;

    @Column(columnDefinition = "datetime")
    private LocalDateTime ngayGioBatDau;
    @Column(columnDefinition = "datetime")
    private LocalDateTime ngayGioKetThuc;
    @Column(columnDefinition = "int")
    private int tongHoaDon;
    @Column(columnDefinition = "double")
    private double tongTienCaTruoc;
    @Column(columnDefinition = "double")
    private double tongKetHoaDon;
    @Column(columnDefinition = "double")
    private double tongKetThucThu;
    @Column(columnDefinition = "double")
    private double thatThoat;
    @Column(columnDefinition = "double")
    private double tongVat;

    @ManyToOne
    @JoinColumn(name = "nhanvien_id")
    private NhanVien nhanvien;

}

