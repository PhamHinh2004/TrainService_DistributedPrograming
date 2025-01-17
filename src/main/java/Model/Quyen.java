package Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@Entity
@Table(name = "quyen")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Quyen {
    @Id
    @Column(name = "quyen_id")
    @EqualsAndHashCode.Include
    private String maQuyen;

    @Column(name = "tenQuyen")
    private String tenQuyen;

    @ManyToOne
    @JoinColumn(name = "trang_id", referencedColumnName = "trang_id", nullable = false)
    private Trang trang;

}