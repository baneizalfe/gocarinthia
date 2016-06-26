package com.baneizalfe.gocarinthia.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by baneizalfe on 6/26/16.
 */
public class PaymentRequest {

    @Expose
    @SerializedName("CardNumner")
    public String cardNumber;

    @Expose
    @SerializedName("CardHolder")
    public String cardHolder;

    @Expose
    @SerializedName("Expiration")
    public String expiration;

    @Expose
    @SerializedName("Cvv")
    public String cvv;

    public PaymentRequest(String cardNumber, String cardHolder, String expiration, String cvv) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.expiration = expiration;
        this.cvv = cvv;
    }
}
