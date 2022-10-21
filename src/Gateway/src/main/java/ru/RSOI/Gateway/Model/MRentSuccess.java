package ru.RSOI.Gateway.Model;

import java.util.UUID;

public class MRentSuccess {

    public UUID rentalUid;
    public String status;
    public UUID carUid;
    public String dateFrom;
    public String dateTo;
    public MRentPaymentInfo payment;

}
