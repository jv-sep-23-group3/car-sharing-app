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
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.sep23.group3.car.sharing.config.ControllerTestConfig;
import mate.sep23.group3.car.sharing.dto.payment.PaymentRequestDto;
import mate.sep23.group3.car.sharing.dto.payment.PaymentResponseDto;
import mate.sep23.group3.car.sharing.model.Payment;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(ControllerTestConfig.class)
class PaymentControllerTest {
    protected static MockMvc mockMvc;
    private static final String CUSTOMER = "customer1@email.com";
    private static final String MANAGER = "manager@email.com";
    private static PaymentRequestDto createPaymentSessionDto;
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

        createPaymentSessionDto = new PaymentRequestDto()
                .setRentalId(1L)
                .setType(Payment.Type.PAYMENT);

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

    @WithUserDetails(MANAGER)
    @Test
    @DisplayName("Get all payments for manager")
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

    @WithUserDetails(CUSTOMER)
    @Test
    @DisplayName("Get all payments for customer")
    void getAllForCustomer_ValidRequest_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/payments")
                )
                .andExpect(status().isOk())
                .andReturn();

        List<PaymentResponseDto> expected = List.of(first, second);
        PaymentResponseDto[] actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(),
                PaymentResponseDto[].class
        );

        Assertions.assertEquals(expected.size(), actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithUserDetails(CUSTOMER)
    @Test
    @DisplayName("Check successful payment")
    @Sql(scripts = "classpath:database/payments/add-payment-for-success-endpoint.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/payments/delete-payment-for-success-endpoint.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void checkSuccessfulPayment_ValidSessionId_ReturnMessage() throws Exception {
        String sessionId = "fourth_id";

        MvcResult mvcResult = mockMvc.perform(
                get("/payments/success")
                        .param("sessionId", sessionId))
                .andExpect(status().isOk())
                .andReturn();

        String expected = "Payment was successful";
        String actual = mvcResult.getResponse().getContentAsString();

        Assertions.assertEquals(expected, actual);
    }

    @WithUserDetails(CUSTOMER)
    @Test
    @DisplayName("Check canceled payment")
    @Sql(scripts = "classpath:database/payments/add-payment-for-cancel-endpoint.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/payments/delete-payment-for-cancel-endpoint.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void pausePayment_ValidSessionId_ReturnMessage() throws Exception {
        String sessionId = "fifth_id";

        MvcResult mvcResult = mockMvc.perform(
                        get("/payments/cancel")
                                .param("sessionId", sessionId)
                )
                .andExpect(status().isOk())
                .andReturn();

        String expected = "You can pay in 24 hours";
        String actual = mvcResult.getResponse().getContentAsString();

        Assertions.assertEquals(expected, actual);
    }
}
