package mate.sep23.group3.car.sharing.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import mate.sep23.group3.car.sharing.model.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;

    @Test
    @DisplayName("Find role by role name")
    void findByRoleName_ReturnManagerRole() {
        Role.RoleName roleNameManager = Role.RoleName.MANAGER;
        Role.RoleName roleNameCustomer = Role.RoleName.CUSTOMER;
        Role.RoleName roleNameAdmin = Role.RoleName.ADMIN;

        Role actualManager = roleRepository.findByRoleName(roleNameManager);
        Role actualCustomer = roleRepository.findByRoleName(roleNameCustomer);
        Role actualAdmin = roleRepository.findByRoleName(roleNameAdmin);

        assertEquals(roleNameManager, actualManager.getRoleName());
        assertEquals(roleNameCustomer, actualCustomer.getRoleName());
        assertEquals(roleNameAdmin, actualAdmin.getRoleName());
    }
}
