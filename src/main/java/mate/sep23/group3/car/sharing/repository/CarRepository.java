package mate.sep23.group3.car.sharing.repository;

import mate.sep23.group3.car.sharing.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}
