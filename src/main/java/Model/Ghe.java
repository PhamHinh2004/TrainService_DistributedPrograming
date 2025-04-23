package Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;
import Enum.LoaiDichVu;

@Data
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
    @ManyToOne
    @JoinColumn(name = "toa_id")
    private Toa toa;

    @OneToMany(mappedBy = "ghe")
    private  Set<Ve> ves;
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Ghe ghe)) return false;
        return Objects.equals(getMaGhe(), ghe.getMaGhe());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMaGhe());
    }
}
