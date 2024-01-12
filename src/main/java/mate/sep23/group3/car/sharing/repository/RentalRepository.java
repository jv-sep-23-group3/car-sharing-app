package mate.sep23.group3.car.sharing.repository;

import mate.sep23.group3.car.sharing.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findAllByUserId(Long userId);
}
