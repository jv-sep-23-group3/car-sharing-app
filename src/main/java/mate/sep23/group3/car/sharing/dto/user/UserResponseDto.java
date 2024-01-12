package mate.sep23.group3.car.sharing.dto.user;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
}
