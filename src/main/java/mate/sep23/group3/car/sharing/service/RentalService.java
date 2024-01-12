package mate.sep23.group3.car.sharing.service;

import java.util.List;
import mate.sep23.group3.car.sharing.dto.rental.RentalRequestDto;
import mate.sep23.group3.car.sharing.dto.rental.RentalResponseDto;
import org.springframework.data.domain.Pageable;

public interface RentalService {
    RentalResponseDto save(RentalRequestDto requestDto, Long userId);

    List<RentalResponseDto> getListByUserIdAndIsActiveStatus(
            Long userId,
            boolean isActive,
            Pageable pageable
    );

    RentalResponseDto findByIdAndUserId(Long id, Long userId);

    RentalResponseDto update(Long id);
}
