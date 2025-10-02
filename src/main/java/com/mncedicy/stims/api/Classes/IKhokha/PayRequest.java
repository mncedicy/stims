package com.mncedicy.stims.api.Classes.IKhokha;

public class PayRequest {
    public String entityID= "";
    public String externalEntityID = "";
    public int amount = 0;   // Note amount is in cents
    public String currency = "ZAR";
    public String requesterUrl = "";
    public String description = "";
    public String paymentReference="";
    public String mode = "live";
    public String externalTransactionID="";
    public Urls urls = new Urls();


}
