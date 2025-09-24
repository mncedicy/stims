package com.mncedicy.stims.api.Classes;

public class PayResponse {
    String responseCode;
    String status;
    String paylinkID;
    String externalTransactionID;

    @Override
    public String toString() {
        return "PayResponse{" +
                "responseCode='" + responseCode + '\'' +
                ", status='" + status + '\'' +
                ", paylinkID='" + paylinkID + '\'' +
                ", externalTransactionID='" + externalTransactionID + '\'' +
                '}';
    }
}
