package Model;

import jakarta.persistence.*;
import lombok.*;

import javax.xml.namespace.QName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LichTrinh {
    @Id
    @EqualsAndHashCode.Include
    private String maLichTrinh;

    private LocalDate ngayKhoiHanh;
    private LocalTime gioKhoiHanh;
    private LocalTime gioDen;
    private String gaKhoiHanh;
    private String gaDen;
    private String moTa;

    // ================================
    @ManyToOne
    @JoinColumn(name = "soHieu")
    private Tau tau;

    @ManyToOne
    @JoinColumn(name = "maNhaGa")
    private NhaGa nhaGa;
}
