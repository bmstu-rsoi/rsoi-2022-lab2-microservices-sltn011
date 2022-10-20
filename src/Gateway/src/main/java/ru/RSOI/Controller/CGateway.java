package ru.RSOI.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CGateway {

    public static final String CarsService    = "localhost:8070/api/v1/sys/cars";
    public static final String RentService    = "localhost:8060/api/v1/sys/rental";
    public static final String PaymentService = "localhost:8050/api/v1/sys/payment";

    @GetMapping("/cars")
    public void getAllCars(@RequestParam int page, @RequestParam int size,
                           @RequestParam(defaultValue = "false") boolean showAll)
    {

    }

}
