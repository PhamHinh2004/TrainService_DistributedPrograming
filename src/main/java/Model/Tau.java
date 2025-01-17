package Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class Tau {
    @Id
    private String soHieu;
    private String soluongToiDa;
    private String quocGia;
    private String chuSoHuu;
    private LocalDateTime ngayBatDauVanHanh;
    @OneToMany(mappedBy = "tau")
    @ToString.Exclude
    private Set<Toa> toas;

    @OneToMany(mappedBy = "tau")
    @ToString.Exclude
    private Set<LichCapBenGa> lichCapBenGas;
}
