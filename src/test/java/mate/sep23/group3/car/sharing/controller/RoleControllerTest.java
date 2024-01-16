package mate.sep23.group3.car.sharing.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLException;
import java.util.List;
import mate.sep23.group3.car.sharing.dto.role.RoleResponseDto;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoleControllerTest {
    protected static MockMvc mockMvc;
    private static final int EXPECTED_LIST_SIZE = 3;
    private static List<RoleResponseDto> expectedList;

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

        expectedList = List.of(
                new RoleResponseDto().setId(1L).setRole("MANAGER"),
                new RoleResponseDto().setId(2L).setRole("CUSTOMER"),
                new RoleResponseDto().setId(3L).setRole("ADMIN"));
    }

    @WithMockUser(username = "user", roles = {"ADMIN"})
    @Test
    @DisplayName("Get all roles")
    void getAll_ListOfThreeRoles_Success() throws Exception {

        MvcResult result = mockMvc.perform(
                        get("/roles"))
                .andExpect(status().isOk())
                .andReturn();

        List<RoleResponseDto> actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), new TypeReference<List<RoleResponseDto>>() {});

        assertEquals(EXPECTED_LIST_SIZE, actual.size());
        assertTrue(CollectionUtils.isEqualCollection(expectedList, actual));
    }
}
