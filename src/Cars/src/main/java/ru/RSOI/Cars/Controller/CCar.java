package ru.RSOI.Cars.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.RSOI.Cars.Model.MCar;
import ru.RSOI.Cars.Repo.RCar;

import ru.RSOI.Cars.Error.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/sys/cars")
public class CCar {

    private final RCar carRepo;

    public CCar(RCar carRepo)
    {
        this.carRepo = carRepo;
    }

    @GetMapping("")
    public Iterable<MCar> getCars(
            @RequestParam int page, @RequestParam int size, @RequestParam(defaultValue = "false") boolean showAll)
    {
        return getCarsPage(page, size, showAll);
    }

    @PostMapping("")
    public ResponseEntity<Object> addPerson(@RequestBody Map<String, String> values)
    {
        MCar car = new MCar();
        fillValues(car, values);
        int newID = carRepo.save(car).getId();

        String stringLocation = String.format("/api/v1/sys/cars/%d", newID);
        URI location = ServletUriComponentsBuilder.fromUriString(stringLocation).build().toUri();
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    public MCar updateAvailableCar(@PathVariable int id, @RequestBody Map<String, String> values)
    {
        MCar car = findCar(id);
        updateAvailability(car, values);
        return carRepo.save(car);
    }

    private Iterable<MCar> getCarsPage(int page, int size, boolean showAll)
    {
        if (showAll)
        {
            return carRepo.findAll(PageRequest.of(page, size));
        }
        else
        {
            return carRepo.findAvailable(PageRequest.of(page, size));
        }
    }

    private MCar findCar(int id)
    {
        return carRepo.findById(id).orElseThrow(() -> new ENotFoundError("Car not found!"));
    }

    private MCar updateAvailability(MCar car, Map<String, String> values)
    {
        if (values.containsKey("availability"))
        {
            car.v8_availability = Boolean.parseBoolean(values.get("availability"));
        }
        return car;
    }

    private MCar fillValues(MCar car, Map<String, String> values)
    {
        if (values.containsKey("brand"))
        {
            car.v2_brand = values.get("brand");
        }
        if (values.containsKey("model"))
        {
            car.v3_model = values.get("model");
        }
        if (values.containsKey("registration_number"))
        {
            car.v4_registration_number = values.get("registration_number");
        }
        if (values.containsKey("power"))
        {
            car.v5_power = Integer.parseInt(values.get("power"));
        }
        if (values.containsKey("price"))
        {
            car.v6_price = Integer.parseInt(values.get("price"));
        }
        if (values.containsKey("type"))
        {
            car.v7_type = values.get("type");
        }
        if (values.containsKey("availability"))
        {
            car.v8_availability = Boolean.parseBoolean(values.get("availability"));
        }

        return car;
    }
}
