package mate.sep23.group3.car.sharing.controller;

import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.service.CarService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Car management", description = "Endpoints for managing cars")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cars")
public class CarController {
    private final CarService carService;
}
