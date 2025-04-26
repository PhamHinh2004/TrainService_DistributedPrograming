package Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Quyen {
    @Id
    @EqualsAndHashCode.Include
    private String maQuyen;

    private String tenQuyen;

// =======================================
    @ManyToMany(mappedBy = "dsQuyen")
    private Set<NhomQuyen> dsNhomQuyen;

}