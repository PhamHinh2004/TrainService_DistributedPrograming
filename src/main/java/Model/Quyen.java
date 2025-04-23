package Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Quyen {
    @Id
    @EqualsAndHashCode.Include
    private String maQuyen;

    private String tenQuyen;

// =======================================
    @ManyToMany(mappedBy = "quyen")
    private Set<NhomQuyen> nhomQuyen;

}