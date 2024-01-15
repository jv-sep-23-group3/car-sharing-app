package mate.sep23.group3.car.sharing.repository;

import java.util.List;
import java.util.Optional;
import mate.sep23.group3.car.sharing.model.Payment;
import mate.sep23.group3.car.sharing.model.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Page<Payment> findAllByRentalIn(List<Rental> rentals, Pageable pageable);

    @EntityGraph(attributePaths = {"rental", "rental.user", "rental.car"})
    Optional<Payment> findBySessionId(String sessionId);
}
