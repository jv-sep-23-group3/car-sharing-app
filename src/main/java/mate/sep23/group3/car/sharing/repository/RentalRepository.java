package mate.sep23.group3.car.sharing.repository;

import java.util.Optional;
import mate.sep23.group3.car.sharing.model.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    @EntityGraph(attributePaths = {"car", "user"})
    Page<Rental> findAllByUserIdAndIsActive(Long userId, Boolean isActive, Pageable pageable);

    @EntityGraph(attributePaths = {"car", "user"})
    Page<Rental> findAllByIsActive(Boolean isActive, Pageable pageable);

    @EntityGraph(attributePaths = {"car", "user"})
    Optional<Rental> findByIdAndUserId(Long id, Long userId);
}
