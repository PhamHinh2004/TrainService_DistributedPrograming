package Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class ChuyenDi {
    @Id
    private String maChuyenDi;
    private String nhaGaKhoiHanh;
    private String nhaGaKetThuc;
    private LocalDateTime thoiGianKhoiHanh;
    private LocalDateTime thoiGianDuKienCapBenDiemCuoi;
    private boolean daHoanThanh;

    @ManyToOne
    @JoinColumn(name = "ghe_id")
    private Ghe ghe;

    @OneToMany(mappedBy = "chuyenDi")
    @ToString.Exclude
    Set<ChiTietTrangThaiGhe> chiTietTrangThaiGhes;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ChuyenDi chuyenDi)) return false;
        return Objects.equals(getMaChuyenDi(), chuyenDi.getMaChuyenDi());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMaChuyenDi());
    }
}
