package Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class Toa {
    @Id
    private String maToa;

    private String tenToa;
    private int soLuongGhe;
    @OneToMany(mappedBy = "toa")
    @ToString.Exclude
    Set<Ghe> ghes;
    @ManyToOne
    @JoinColumn(name = "toa_id")
    private Tau tau;
}
