package mate.sep23.group3.car.sharing.dto.user.profile;

import lombok.Data;

@Data
public class UserWithNameAndLastNameResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
}
