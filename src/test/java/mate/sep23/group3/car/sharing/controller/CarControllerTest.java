package mate.sep23.group3.car.sharing.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import mate.sep23.group3.car.sharing.dto.car.CarRequestDto;
import mate.sep23.group3.car.sharing.dto.car.CarResponseDto;
import mate.sep23.group3.car.sharing.model.Car;
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
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@Sql(scripts = {"classpath:database/cars/add-car-to-cars-table.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:database/cars/delete-car-from-cars-table.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarControllerTest {
    protected static MockMvc mockMvc;
    private static final Long INDEX_OF_EXISTING_CAR = 1L;
    private static final Long INDEX_OF_UPDATING_CAR = 2L;
    private static final Long INDEX_OF_NOT_EXISTING_CAR = 20L;
    private static final int EXPECTED_LIST_SIZE = 3;
    private static CarRequestDto requestDto;
    private static CarRequestDto emptyBrandRequestDto;
    private static CarRequestDto updateExistingCarRequestDto;
    private static CarResponseDto responseDto;
    private static CarResponseDto updatedResponseDto;
    private static CarResponseDto responseDtoIdOne;
    private static List<CarResponseDto> expectedList;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();

        requestDto = new CarRequestDto()
                .setBrand("Volvo")
                .setModel("V80")
                .setType(Car.Type.SEDAN)
                .setInventory(3)
                .setDailyFee(BigDecimal.valueOf(80));

        emptyBrandRequestDto = new CarRequestDto()
                .setModel("V80")
                .setType(Car.Type.SEDAN)
                .setInventory(3)
                .setDailyFee(BigDecimal.valueOf(80));

        updateExistingCarRequestDto = new CarRequestDto()
                .setBrand("Mercedes")
                .setModel("E212")
                .setType(Car.Type.SEDAN)
                .setInventory(5)
                .setDailyFee(BigDecimal.valueOf(65));

        responseDto = new CarResponseDto()
                .setId(10L)
                .setBrand(requestDto.getBrand())
                .setModel(requestDto.getModel())
                .setType(requestDto.getType())
                .setInventory(requestDto.getInventory())
                .setDailyFee(requestDto.getDailyFee());

        updatedResponseDto = new CarResponseDto()
                .setId(INDEX_OF_UPDATING_CAR)
                .setBrand(updateExistingCarRequestDto.getBrand())
                .setModel(updateExistingCarRequestDto.getModel())
                .setType(updateExistingCarRequestDto.getType())
                .setInventory(updateExistingCarRequestDto.getInventory())
                .setDailyFee(updateExistingCarRequestDto.getDailyFee());

        responseDtoIdOne = new CarResponseDto()
                .setId(1L)
                .setBrand("BMW")
                .setModel("X5")
                .setInventory(5)
                .setType(Car.Type.SUV)
                .setDailyFee(BigDecimal.valueOf(120).setScale(2));

        expectedList = List.of(
                new CarResponseDto().setId(1L).setBrand("BMW").setModel("X5")
                        .setType(Car.Type.SUV).setInventory(5)
                        .setDailyFee(BigDecimal.valueOf(120).setScale(2)),
                new CarResponseDto().setId(2L).setBrand("Mercedes").setModel("E212")
                        .setType(Car.Type.SEDAN).setInventory(4)
                        .setDailyFee(BigDecimal.valueOf(70).setScale(2)),
                new CarResponseDto().setId(3L).setBrand("Audi").setModel("A6")
                        .setType(Car.Type.UNIVERSAL).setInventory(3)
                        .setDailyFee(BigDecimal.valueOf(80).setScale(2)));
    }

    @WithMockUser(username = "user", roles = {"CUSTOMER", "MANAGER"})
    @Test
    @DisplayName("Get car by existing id")
    void getById_ExistingId_Success() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/cars/{id}", INDEX_OF_EXISTING_CAR))
                .andExpect(status().isOk())
                .andReturn();

        CarResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CarResponseDto.class);

        assertNotNull(actual);
        assertEquals(responseDtoIdOne, actual);
    }

    @WithMockUser(username = "user", roles = {"CUSTOMER", "MANAGER"})
    @Test
    @DisplayName("Get car by not existing id")
    void getById_NotExistingId_NotFound() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/cars/{id}", INDEX_OF_NOT_EXISTING_CAR))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @WithMockUser(username = "user", roles = {"CUSTOMER", "MANAGER"})
    @Test
    @DisplayName("Get all cars")
    void getAll_ListOfThreeCars_Success() throws Exception {

        MvcResult result = mockMvc.perform(
                        get("/cars"))
                .andExpect(status().isOk())
                .andReturn();

        List<CarResponseDto> actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), new TypeReference<List<CarResponseDto>>() {});

        assertEquals(EXPECTED_LIST_SIZE, actual.size());
        assertIterableEquals(expectedList, actual);
    }

    @WithMockUser(username = "user", roles = {"MANAGER"})
    @Test
    @DisplayName("Add a new car")
    void add_ValidRequestDto_Success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/cars")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CarResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CarResponseDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(responseDto, actual, "id"));
    }

    @WithMockUser(username = "user", roles = {"MANAGER"})
    @Test
    @DisplayName("Add a new car, empty brand")
    void add_NotValidRequestDto_BadRequest() throws Exception {

        String jsonRequest = objectMapper.writeValueAsString(emptyBrandRequestDto);

        MvcResult result = mockMvc.perform(
                        post("/cars")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @WithMockUser(username = "user", roles = {"MANAGER"})
    @Test
    @DisplayName("Update car by id")
    void update_ValidRequestDto_Success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(updateExistingCarRequestDto);

        MvcResult result = mockMvc.perform(
                        put("/cars/{id}", INDEX_OF_UPDATING_CAR)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CarResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CarResponseDto.class);

        assertNotNull(actual);
        assertEquals(updatedResponseDto, actual);
    }

    @WithMockUser(username = "user", roles = {"MANAGER"})
    @Test
    @DisplayName("Update car by not existing id")
    void update_NotExistingId_NotFound() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(updateExistingCarRequestDto);

        MvcResult result = mockMvc.perform(
                        put("/cars/{id}", INDEX_OF_NOT_EXISTING_CAR)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @WithMockUser(username = "user", roles = {"MANAGER"})
    @Test
    @DisplayName("Update car, empty brand")
    void update_NotValidRequestDto_BadRequest() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(emptyBrandRequestDto);

        MvcResult result = mockMvc.perform(
                        put("/cars/{id}", INDEX_OF_UPDATING_CAR)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
