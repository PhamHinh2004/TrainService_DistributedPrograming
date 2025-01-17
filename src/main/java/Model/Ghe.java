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
public class Ghe {
    @Id
    private String maGhe;
    private String viTriGhe;
    @Enumerated(EnumType.STRING)
    private LoaiGhe loaiGhe;

    @OneToMany(mappedBy = "ghe")
    @ToString.Exclude
    Set<ChuyenDi> chuyenDis;

    @ManyToOne
    @JoinColumn(name = "toa_id")
    private Toa toa;

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
