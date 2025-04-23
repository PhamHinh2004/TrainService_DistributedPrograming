package Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LichTrinh {
    @Id
    private String maLichTrinh;

    private LocalDate ngayKhoiHanh;
    private LocalTime gioKhoiHanh;
    private LocalTime gioDen;
    private String gaKhoiHanh;
    private String gaDen;
    private String moTa;

    // ================================
    @ManyToOne
    @JoinColumn(name = "tau_id")
    private Tau tau;

    @ManyToOne
    @JoinColumn(name = "nhaga_id")
    private NhaGa nhaGa;
}
