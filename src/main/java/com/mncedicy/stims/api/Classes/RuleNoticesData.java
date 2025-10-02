package com.mncedicy.stims.api.Classes;

import com.mncedicy.stims.api.Model.infringement_notice;
import com.mncedicy.stims.api.Model.invoice;
import com.mncedicy.stims.api.Model.rule;
import com.mncedicy.stims.api.Model.rule_receiver;

import java.util.ArrayList;
import java.util.List;

public class RuleNoticesData {
    public rule rule;
    public List<infringement_notice> notices;
    public List<invoice> invoices;

    public RuleNoticesData() {
        this.notices = new ArrayList<>();
        this.invoices = new ArrayList<>();
    }
    public RuleNoticesData(rule rule, List<infringement_notice> notices) {
        this.rule = rule;
        this.notices = notices;
        this.invoices = new ArrayList<>();
    }
    public RuleNoticesData(rule rule) {
        this.rule = rule;
        this.notices = new ArrayList<>();
        this.invoices = new ArrayList<>();
    }
}
