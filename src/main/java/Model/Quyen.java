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


    @ManyToMany(mappedBy = "quyen")
    private Set<Trang> trang;

}