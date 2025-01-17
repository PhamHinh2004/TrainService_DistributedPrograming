package Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LichCapBenGa {
    @Id
    private String maLich;
    private LocalDateTime gioCapBen;
    private LocalDateTime gioKhoiHanh;
    private String moTa;

    @ManyToOne
    @JoinColumn(name = "tau_id")
    private Tau tau;

    @ManyToOne
    @JoinColumn(name = "nhaga_id")
    private NhaGa nhaGa;
}
