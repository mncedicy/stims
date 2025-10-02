package com.mncedicy.stims.api.Classes.Yoco;

public class LineItem {
    public int quantity;
    public String displayName;
    public PricingDetails pricingDetails = new PricingDetails();
    public int taxAmount;
    public int discountAmount;
    public String description;
    public String uuid;
}
