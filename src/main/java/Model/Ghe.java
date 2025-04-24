package Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;
import Enum.LoaiDichVu;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Ghe {
    @Id
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

    @Override
    public int hashCode() {
        return Objects.hash(getMaGhe());
    }
}
