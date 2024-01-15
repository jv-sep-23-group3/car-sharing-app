package mate.sep23.group3.car.sharing.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;


@Entity
@Data
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "role_name", nullable = false, unique = true)
    private RoleName roleName;

    @Override
    public String getAuthority() {
        return "ROLE_" + roleName.name();
    }


    public enum RoleName {
        MANAGER(0), CUSTOMER(1), ADMIN(2);

        private final int index;

        RoleName(int index) {
            this.index = index;
        }

        public Integer getIndex() {
            return index;
        }

        public static RoleName getByIndex(int index) {
            for (RoleName roleName : RoleName.values()) {
                if (roleName.getIndex() == index) {
                    return roleName;
                }
            }
            throw new IllegalArgumentException("No enum constant with index: " + index);
        }

    }
}
