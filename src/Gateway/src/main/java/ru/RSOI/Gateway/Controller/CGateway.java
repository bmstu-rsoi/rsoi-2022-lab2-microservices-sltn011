package ru.RSOI.Gateway.Controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import ru.RSOI.Gateway.Model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class CGateway {

    public static final String CarsService    = "http://localhost:8070/api/v1/sys/cars";
    public static final String RentService    = "http://localhost:8060/api/v1/sys/rental";
    public static final String PaymentService = "http://localhost:8050/api/v1/sys/payment";

    @GetMapping("/cars")
    public MCarsPage getAvailableCars(@RequestParam int page, @RequestParam int size,
                                      @RequestParam(defaultValue = "false") boolean showAll)
    {
        String url = UriComponentsBuilder.fromHttpUrl(CarsService)
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("showAll", showAll)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestOperations restOperations = new RestTemplate();
        HttpEntity<String> response = restOperations.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );
        JSONObject obj = new JSONObject(response.getBody());

        int totalElements = obj.getInt("totalElements");

        JSONArray jsonCars = obj.getJSONArray("content");
        int numCars = jsonCars.length();
        ArrayList<MCarInfo> carsInfo = new ArrayList<>(numCars);
        for (int i = 0; i < numCars; ++i)
        {
            JSONObject jsonCar = jsonCars.getJSONObject(i);
            MCarInfo carInfo = parseCarInfo(jsonCar);
            carsInfo.add(carInfo);
        }

        MCarsPage carsPage = new MCarsPage(page, size, totalElements, carsInfo);
        return carsPage;
    }

    @GetMapping("/rental")
    public List<MRentInfo> getAllUserRents(@RequestHeader(value = "X-User-Name") String username)
    {
        String url = UriComponentsBuilder.fromHttpUrl(RentService)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("X-User-Name", username);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestOperations restOperations = new RestTemplate();
        HttpEntity<String> response = restOperations.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        JSONArray rents = new JSONArray(response.getBody());
        int numRents = rents.length();
        ArrayList<MRentInfo> rentsInfo = new ArrayList<>(numRents);
        for (int i = 0; i < numRents; ++i)
        {
            JSONObject rentInfo = rents.getJSONObject(i);
            rentsInfo.add(getRentInfoFromJSON(rentInfo));
        }

        return rentsInfo;
    }

    @PostMapping("/rental")
    public MRentSuccess tryRenting(@RequestHeader(value = "X-User-Name") String username,
                                   @RequestBody Map<String, String> values)
    {
        // TODO : MCarInfo car = requestAvailableCar();
        return null;
    }

    @GetMapping("/rental/{rentalUid}")
    public MRentInfo getUserRent(@PathVariable String rentalUid, @RequestHeader(value = "X-User-Name") String username)
    {

        return null;
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

    private MRentCarInfo getRentCarInfo(String carUid, String username)
    {
        String url = UriComponentsBuilder.fromHttpUrl(CarsService + "/" + carUid)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("X-User-Name", username);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestOperations restOperations = new RestTemplate();
        HttpEntity<String> response = restOperations.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        JSONObject obj = new JSONObject(response.getBody());

        UUID carUidVal = UUID.fromString(carUid);
        String brand = obj.getString("v2_brand");
        String model = obj.getString("v3_model");
        String registrationNumber = obj.getString("v4_registration_number");

        return new MRentCarInfo(carUidVal, brand, model, registrationNumber);
    }

    private MRentPaymentInfo getRentPaymentInfo(String paymentUid)
    {
        String url = UriComponentsBuilder.fromHttpUrl(RentService + "/" + paymentUid)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestOperations restOperations = new RestTemplate();
        HttpEntity<String> response = restOperations.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        JSONObject obj = new JSONObject(response.getBody());

        UUID paymentUidVal = UUID.fromString(paymentUid);
        String status = obj.getString("v2_status");
        int price = obj.getInt("v3_price");

        return new MRentPaymentInfo(paymentUidVal, status, price);
    }

    private MRentInfo getRentInfoFromJSON(JSONObject obj)
    {
        UUID rentalUid = UUID.fromString(obj.getString("v1_rental_uid"));
        String status = obj.getString("v7_status");
        String dateFrom = obj.getString("v5_date_from");
        String dateTo = obj.getString("v6_date_to");

        dateFrom = dateFrom.substring(0, dateFrom.indexOf('T'));
        dateTo = dateTo.substring(0, dateTo.indexOf('T'));

        MRentCarInfo rentCarInfo =
                getRentCarInfo(obj.getString("v4_car_uid"), obj.getString("v2_username"));

        MRentPaymentInfo rentPaymentInfo = getRentPaymentInfo(obj.getString("v3_payment_uid"));

        return new MRentInfo(rentalUid, status, dateFrom, dateTo, rentCarInfo, rentPaymentInfo);
    }

    private MCarInfo parseCarInfo(JSONObject obj)
    {
        UUID carUid = UUID.fromString(obj.getString("v1_car_uid"));
        String brand = obj.getString("v2_brand");
        String model = obj.getString("v3_model");
        String registrationNumber = obj.getString("v4_registration_number");
        int power = obj.getInt("v5_power");
        String type = obj.getString("v7_type");
        int price = obj.getInt("v6_price");
        boolean available = obj.getBoolean("v8_availability");

        return new MCarInfo(carUid, brand, model, registrationNumber, power, type, price, available);
    }
    
    private MCarInfo requestAvailableCar(String carUid)
    {
        String url = UriComponentsBuilder.fromHttpUrl(CarsService + "/request/" + carUid)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestOperations restOperations = new RestTemplate();
        HttpEntity<String> response = restOperations.exchange(
                url,
                HttpMethod.PATCH,
                entity,
                String.class
        );

        JSONObject obj = new JSONObject(response.getBody());
        return parseCarInfo(obj);
    }
}
