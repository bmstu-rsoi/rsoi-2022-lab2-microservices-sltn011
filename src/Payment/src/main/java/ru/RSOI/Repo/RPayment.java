package ru.RSOI.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.RSOI.Model.MPayment;

import java.util.List;
import java.util.UUID;

@Repository
public interface RPayment extends JpaRepository<MPayment, Integer> {

    @Query("SELECT p FROM payment p where p.payment_uid = payment_uid")
    public List<MPayment> findPayment(UUID payment_uid);

}
