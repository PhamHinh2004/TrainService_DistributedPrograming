package Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
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
