package com.mncedicy.stims.api.Classes;

import com.mncedicy.stims.api.Model.*;

import java.util.List;

public class InfringementData {
    public infringement_notice notice;
    public infringement_infringer infringer;
    public charge_code charge_code;
    public InvoiceData invoiceData;
    public List<history> historyList;
}
