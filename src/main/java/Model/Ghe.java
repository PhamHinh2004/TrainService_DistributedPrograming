package Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;
import Enum.LoaiDichVu;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Ghe {
    @Id
    @EqualsAndHashCode.Include
    private String maGhe;
    private String viTriGhe;

    @Enumerated(EnumType.STRING)
    private LoaiDichVu loaiGhe;


// ================================
    @OneToMany (mappedBy = "ghe")
    private Set<Ve> dsVe;

    @ManyToOne
    @JoinColumn(name = "maToa")
    private Toa toa;

}
