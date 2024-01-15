package mate.sep23.group3.car.sharing.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.sep23.group3.car.sharing.dto.payment.PaymentResponseDto;
import mate.sep23.group3.car.sharing.model.Payment;
import mate.sep23.group3.car.sharing.model.Role;
import mate.sep23.group3.car.sharing.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentControllerTest {
    protected static MockMvc mockMvc;
    private static PaymentResponseDto first;
    private static PaymentResponseDto second;
    private static PaymentResponseDto third;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();

        teardown(dataSource);

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/cars/add-car-to-cars-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/users/add-users-to-users-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/users/roles/"
                            + "add-users-roles-to-users-roles-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/rentals/add-rental-to-rentals-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/payments/add-payments-to-payments-table.sql")
            );
        }

        first = new PaymentResponseDto()
                .setId(1L)
                .setStatus(Payment.Status.PENDING)
                .setType(Payment.Type.PAYMENT)
                .setRentalId(1L)
                .setSession("first_url")
                .setSessionId("first_id")
                .setAmount(BigDecimal.valueOf(1920.00).setScale(2, RoundingMode.UP));

        second = new PaymentResponseDto()
                .setId(2L)
                .setStatus(Payment.Status.PAID)
                .setType(Payment.Type.PAYMENT)
                .setRentalId(2L)
                .setSession("second_url")
                .setSessionId("second_id")
                .setAmount(BigDecimal.valueOf(1120.00).setScale(2, RoundingMode.UP));

        third = new PaymentResponseDto()
                .setId(3L)
                .setStatus(Payment.Status.PENDING)
                .setType(Payment.Type.PAYMENT)
                .setRentalId(3L)
                .setSession("third_url")
                .setSessionId("third_id")
                .setAmount(BigDecimal.valueOf(1280.00).setScale(2, RoundingMode.UP));
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/payments/"
                            + "delete-payments-from-payments-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/rentals/delete-rental-from-rentals-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/users/roles/"
                            + "delete-users-roles-from-users-roles-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/users/delete-users-from-users-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/cars/delete-car-from-cars-table.sql")
            );
        }
    }

    @BeforeEach
    void setUp() {
        Role role = new Role()
                .setId(1L)
                .setRoleName(Role.RoleName.MANAGER);

        User user = new User()
                .setId(2L)
                .setEmail("manager@mail.com")
                .setPassword("$2a$10$yCASXP59HTCOdYDPCt3W7.dBNYpo/o99j2ywUg6jGhYLoFaRp.k.G")
                .setRoles(Set.of(role));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @WithMockUser(username = "Manager", roles = {"MANAGER"})
    @Test
    void getAllForManager_ValidRequest_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/payments")
                )
                .andExpect(status().isOk())
                .andReturn();

        List<PaymentResponseDto> expected = List.of(first, second, third);
        PaymentResponseDto[] actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(),
                PaymentResponseDto[].class
        );

        Assertions.assertEquals(expected.size(), actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }
}
