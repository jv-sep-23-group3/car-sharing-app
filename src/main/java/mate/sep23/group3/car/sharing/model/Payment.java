package mate.sep23.group3.car.sharing.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Entity
@Accessors(chain = true)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Status status;
    @Column(nullable = false)
    private Type type;
    @Column(nullable = false)
    private Object session; //TO CHANGE
    @Column(nullable = false, unique = true)
    private String sessionId;
    @Column(nullable = false)
    private BigDecimal amount;


    public enum Status {
        PENDING,
        PAID
    }

    public enum Type {
        PAYMENT,
        FINE
    }
}
