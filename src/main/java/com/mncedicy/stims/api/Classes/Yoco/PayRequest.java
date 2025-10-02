package com.mncedicy.stims.api.Classes.Yoco;

import com.mncedicy.stims.api.Classes.IKhokha.Urls;

import java.util.ArrayList;
import java.util.List;

public class PayRequest {
    public String id;
    public String redirectUrl;
    public String status;
    public String type;
    public String paymentId;
    public int amount = 0;   // Note amount is in cents
    public String currency = "ZAR";
    public String successUrl;
    public String failureUrl;
    public String cancelUrl;
    public String merchantId;
    public String totalDiscount;
    public String totalTaxAmount;
    public String subtotalAmount;
    public String externalId;
    public String processingMode;
    public String mode;
    public String createdDate;
    public Metadata metadata = new Metadata();
    public PaymentMethodDetails paymentMethodDetails = new PaymentMethodDetails();
    public List<LineItem> lineItems = new ArrayList<>();



}
