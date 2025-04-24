package Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

import Enum.TrangThaiTau;

import javax.xml.namespace.QName;

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
    private Set<Toa> dsToa;

    @OneToMany(mappedBy = "tau")
    private Set<LichTrinh> dsLichTrinh;
}
