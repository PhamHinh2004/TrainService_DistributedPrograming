package Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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
    private int soLuongGhe;
    @OneToMany(mappedBy = "toa")
    @ToString.Exclude
    Set<Ghe> ghes;
    @ManyToOne
    @JoinColumn(name = "toa_id")
    private Tau tau;
}
