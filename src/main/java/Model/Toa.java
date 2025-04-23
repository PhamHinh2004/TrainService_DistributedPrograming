package Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import Enum.LoaiDichVu;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Toa {
    @Id
    private String maToa;

    private String tenToa;

    @Enumerated(EnumType.STRING)
    private LoaiDichVu loaToa;

    private int soLuongCho;

// ======================================
    @OneToMany(mappedBy = "toa")
    @ToString.Exclude
    Set<Ghe> ghe;


    @ManyToOne
    @JoinColumn(name = "toa_id")
    private Tau tau;
}
