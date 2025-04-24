package Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import Enum.LoaiDichVu;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Toa {
    @Id
    @EqualsAndHashCode.Include
    private String maToa;

    private String tenToa;

    @Enumerated(EnumType.STRING)
    private LoaiDichVu loaToa;

    private int soLuongCho;

// ======================================
    @OneToMany(mappedBy = "toa")
    private Set<Ghe> dsGhe;

    @ManyToOne
    @JoinColumn(name = "soHieu")
    private Tau tau;
}
