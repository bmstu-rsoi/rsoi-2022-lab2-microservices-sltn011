package ru.RSOI.Gateway.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.RSOI.Gateway.Model.MCarsPage;
import ru.RSOI.Gateway.Model.MRentInfo;
import ru.RSOI.Gateway.Model.MRentSuccess;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CGateway {

    public static final String CarsService    = "localhost:8070/api/v1/sys/cars";
    public static final String RentService    = "localhost:8060/api/v1/sys/rental";
    public static final String PaymentService = "localhost:8050/api/v1/sys/payment";

    @GetMapping("/cars")
    public MCarsPage getAvailableCars(@RequestParam int page, @RequestParam int size,
                                      @RequestParam(defaultValue = "false") boolean showAll)
    {

    }

    @GetMapping("/rental")
    public List<MRentInfo> getAllUserRents(@RequestHeader(value = "X-User-Name") String username)
    {

    }

    @PostMapping("/rental")
    public MRentSuccess tryRenting(@RequestHeader(value = "X-User-Name") String username,
                                   @RequestBody Map<String, String> values)
    {

    }

    @GetMapping("/rental/{rentalUid}")
    public MRentInfo getUserRent(@PathVariable String rentalUid, @RequestHeader(value = "X-User-Name") String username)
    {

    }

    @DeleteMapping("/rental/{rentalUid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelUserRent(@PathVariable String rentalUid, @RequestHeader(value = "X-User-Name") String username)
    {

    }

    @PatchMapping("/rental/{rentalUid}/finish")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void finishUserRent(@PathVariable String rentalUid, @RequestHeader(value = "X-User-Name") String username)
    {

    }


}
