package Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietTrangThaiGhe {
    @Id
    private String maTrangThaiGhe;
    private String trangThaiGhe;
    private String tenNhaGa;

    @ManyToOne
    @JoinColumn(name = "chuyendi_id")
    private ChuyenDi chuyenDi;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ChiTietTrangThaiGhe that)) return false;
        return Objects.equals(getMaTrangThaiGhe(), that.getMaTrangThaiGhe());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMaTrangThaiGhe());
    }
}
