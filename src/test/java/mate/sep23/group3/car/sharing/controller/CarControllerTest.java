package mate.sep23.group3.car.sharing.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import mate.sep23.group3.car.sharing.dto.car.CarRequestDto;
import mate.sep23.group3.car.sharing.dto.car.CarResponseDto;
import mate.sep23.group3.car.sharing.model.Car;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
            ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Get all cars")
    void getAll() {
    }

    @Test
    void getById() {
    }

    @WithMockUser(username = "manager", roles = {"MANAGER"})
    @Test
    @Sql(
            scripts = "classpath:database/cars/delete-first-test-car.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Add a new car")
    void add_ValidRequestDto_Success() throws Exception {
        // Given
        CarRequestDto requestDto = new CarRequestDto();
        requestDto.setModel("Camry");
        requestDto.setBrand("Toyota");
        requestDto.setType(Car.Type.SEDAN);
        requestDto.setInventory(10);
        requestDto.setDailyFee(BigDecimal.valueOf(100));

        CarResponseDto expected = new CarResponseDto();
        expected.setModel(requestDto.getModel());
        expected.setBrand(requestDto.getBrand());
        expected.setType(requestDto.getType());
        expected.setInventory(requestDto.getInventory());
        expected.setDailyFee(requestDto.getDailyFee());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult result = mockMvc.perform(
                post("/cars")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        CarResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CarResponseDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}