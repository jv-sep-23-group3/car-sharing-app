package mate.sep23.group3.car.sharing.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.type.SqlTypes;

@Data
@NoArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE cars SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "cars")
@Accessors(chain = true)
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false, unique = true)
    private String model;

    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private Type type;

    @Column(nullable = false)
    private int inventory;

    @Column(nullable = false)
    private BigDecimal dailyFee;

    @ToString.Exclude
    @Column(nullable = false)
    private boolean isDeleted = false;

    public enum Type {
        SEDAN,
        SUV,
        HATCHBACK,
        UNIVERSAL
    }
}
