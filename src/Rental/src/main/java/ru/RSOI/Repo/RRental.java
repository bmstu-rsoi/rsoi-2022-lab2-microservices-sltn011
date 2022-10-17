package ru.RSOI.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.RSOI.Model.MRental;

import java.util.List;
import java.util.UUID;

@Repository
public interface RRental extends JpaRepository<MRental, Integer> {

    @Query("SELECT r FROM rental r where r.username = username")
    public List<MRental> findUserRents(String username);

    @Query("SELECT r FROM rental r where (r.car_uid = car_uid AND r.status = status)")
    public List<MRental> findCarRents(UUID car_uid, String status);

    @Query("SELECT r FROM rental r where r.rental_uid = rental_uid")
    public List<MRental> findRentByUUID(UUID rental_uid);

}
