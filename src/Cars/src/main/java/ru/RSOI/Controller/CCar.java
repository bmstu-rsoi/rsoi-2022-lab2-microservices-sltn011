package ru.RSOI.Controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.RSOI.Model.MCar;
import ru.RSOI.Repo.RCar;

import ru.RSOI.Error.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cars")
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

    @PatchMapping("/{id}")
    public MCar patchCar(@PathVariable int id, @RequestBody Map<String, String> values)
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
}
