package ru.RSOI.Cars.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.RSOI.Cars.Model.MCar;

@Repository
public interface RCar extends JpaRepository<MCar, Integer> {

    @Query("SELECT c FROM MCar c where c.v8_availability = true")
    public Page<MCar> findAvailable(Pageable pageable);

}
