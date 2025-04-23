package Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

import Enum.TrangThaiTau;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Tau {
    @Id
    private String soHieu;

    private int soluongToa;
    private String quocGia;
    private String chuSoHuu;
    private LocalDateTime ngayBatDauVanHanh;

    @Enumerated(EnumType.STRING)
    private TrangThaiTau trangThai;

// ============================
    @OneToMany(mappedBy = "tau")
    @ToString.Exclude
    private Set<Toa> toas;

    @OneToMany(mappedBy = "tau")
    @ToString.Exclude
    private Set<LichTrinh> lichCapBenGases;
}
