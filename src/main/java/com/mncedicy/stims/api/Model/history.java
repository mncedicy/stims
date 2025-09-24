package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class history implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long history_id;
    public long history_reference_id;
    public String history_reference_type;
    public String history_action;
    @Column(length = 5000)
    public String history_description;
    public LocalDateTime history_action_date =LocalDateTime.now();
    public LocalDateTime history_timestamp=LocalDateTime.now();
    public int history_client_id;
    public String history_status = "New";
    @Column(length = 500)
    public String history_value;
    @Column(length = 500)
    public String history_value1;
    @Column(length = 5000)
    public String history_value2;
    @Column(length = 500)
    public String history_value3;
    @Column(length = 500)
    public String history_value4;
    @Column(length = 500)
    public String history_value5;
    @Column(length = 500)
    public String history_value6;
    @Column(length = 500)
    public String history_value7;
    @Column(length = 500)
    public String history_value8;
    @Column(length = 500)
    public String history_value9;
    public long history_action_by;
    public String history_action_by_name;
    public String history_category;
    public int history_year = LocalDateTime.now().getYear();
    public int history_month = LocalDateTime.now().getMonthValue();

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
