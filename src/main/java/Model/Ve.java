package Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import Enum.LoaiVe;
import Enum.TrangThaiVe;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Ve {
    @Id
    @EqualsAndHashCode.Include
    private String maVe;

    private String maKhachHang;
    private String maHoaDon;

    @Enumerated(EnumType.STRING)
    private LoaiVe loaiVe;

    private double giaVe;
    private int sttGaKhoiHnah;
    private int sttGaDen;
    private LocalDateTime ngayDat;
    private LocalDateTime ngayChinhSuaGanNhat;

    @Enumerated(EnumType.STRING)
    private TrangThaiVe trangThaiVe;

    // =================================
    @ManyToOne
    @JoinColumn(name = "ghe_id")
    private Ghe ghe;


}
