package mate.sep23.group3.car.sharing.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import java.util.Set;
import mate.sep23.group3.car.sharing.model.Role;
import mate.sep23.group3.car.sharing.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    private static final String NOT_EXISTING_EMAIL = "not_existing@email.com";
    private static Role userRole;
    private static User user;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    static void beforeAll() {
        userRole = new Role();
        userRole.setId(3L);
        userRole.setRoleName(Role.RoleName.ADMIN);

        user = new User();
        user.setId(1L);
        user.setEmail("admin@email.com");
        user.setPassword("$2a$12$k/TROQT1JX4fZDIOH4xOA.SEDPJWz.oc9Dy.9IrPxU4ieC9u3Yn7i");
        user.setFirstName("Admin");
        user.setLastName("Admin");
        user.setRoles(Set.of(userRole));
    }

    @Test
    @DisplayName("Find user by email")
    @Sql(scripts = {
            "classpath:database/users/add-users-to-users-table.sql",
            "classpath:database/users/roles/add-users-roles-to-users-roles-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/users/roles/delete-users-roles-from-users-roles-table.sql",
            "classpath:database/users/delete-users-from-users-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByEmail_ExistingEmail_ReturnUser() {

        Optional<User> actual = userRepository.findByEmail(user.getEmail());

        assertEquals(Optional.of(user), actual);
        assertEquals(user.getRoles(), actual.get().getRoles());
    }

    @Test
    @DisplayName("Find user by not existing email")
    @Sql(scripts = {
            "classpath:database/users/add-users-to-users-table.sql",
            "classpath:database/users/roles/add-users-roles-to-users-roles-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/users/roles/delete-users-roles-from-users-roles-table.sql",
            "classpath:database/users/delete-users-from-users-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByEmail_NotExistingEmail_ReturnEmptyOptional() {
        Optional<User> actual = userRepository.findByEmail(NOT_EXISTING_EMAIL);

        assertEquals(Optional.empty(), actual);
    }
}
