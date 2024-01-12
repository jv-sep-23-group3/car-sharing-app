package mate.sep23.group3.car.sharing.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
}
