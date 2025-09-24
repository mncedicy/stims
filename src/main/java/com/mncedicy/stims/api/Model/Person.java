package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long person_id;
    @Column
    public int person_client_id;
    @Column
    public String person_title;
    @Column
    public String person_nationality;
    @Column
    public String person_citizenship;
    @Column
    public String person_initials;
    @Column
    public String person_first_name;
    @Column
    public String person_last_name;
    @Column
    public long person_contacts_id;
    @Column
    public String person_id_number;

    @Column
    public String person_marital_status;
    @Column
    public String person_gender;
    @Column
    public String person_home_language;
    @Column
    public String person_ethnicity;
    @Column
    public String person_signature;
    @Column
    public LocalDateTime person_timestamp= LocalDateTime.now();

    public long getPerson_id() {
        return person_id;
    }

    public void setPerson_id(long person_id) {
        this.person_id = person_id;
    }

    public int getPerson_client_id() {
        return person_client_id;
    }

    public void setPerson_client_id(int person_client_id) {
        this.person_client_id = person_client_id;
    }

    public String getPerson_title() {
        return person_title;
    }

    public void setPerson_title(String person_title) {
        this.person_title = person_title;
    }

    public String getPerson_nationality() {
        return person_nationality;
    }

    public void setPerson_nationality(String person_nationality) {
        this.person_nationality = person_nationality;
    }

    public String getPerson_citizenship() {
        return person_citizenship;
    }

    public void setPerson_citizenship(String person_citizenship) {
        this.person_citizenship = person_citizenship;
    }

    public String getPerson_initials() {
        return person_initials;
    }

    public void setPerson_initials(String person_initials) {
        this.person_initials = person_initials;
    }

    public String getPerson_first_name() {
        return person_first_name;
    }

    public void setPerson_first_name(String person_first_name) {
        this.person_first_name = person_first_name;
    }

    public String getPerson_last_name() {
        return person_last_name;
    }

    public void setPerson_last_name(String person_last_name) {
        this.person_last_name = person_last_name;
    }

    public long getPerson_contacts_id() {
        return person_contacts_id;
    }

    public void setPerson_contacts_id(long person_contacts_id) {
        this.person_contacts_id = person_contacts_id;
    }

    public String getPerson_id_number() {
        return person_id_number;
    }

    public void setPerson_id_number(String person_id_number) {
        this.person_id_number = person_id_number;
    }


    public String getPerson_marital_status() {
        return person_marital_status;
    }

    public void setPerson_marital_status(String person_marital_status) {
        this.person_marital_status = person_marital_status;
    }

    public String getPerson_gender() {
        return person_gender;
    }

    public void setPerson_gender(String person_gender) {
        this.person_gender = person_gender;
    }

    public String getPerson_home_language() {
        return person_home_language;
    }

    public void setPerson_home_language(String person_home_language) {
        this.person_home_language = person_home_language;
    }

    public String getPerson_ethnicity() {
        return person_ethnicity;
    }

    public void setPerson_ethnicity(String person_ethnicity) {
        this.person_ethnicity = person_ethnicity;
    }

    public String getPerson_signature() {
        return person_signature;
    }

    public void setPerson_signature(String person_signature) {
        this.person_signature = person_signature;
    }

    public LocalDateTime getPerson_timestamp() {
        return person_timestamp;
    }

    public void setPerson_timestamp(LocalDateTime person_timestamp) {
        this.person_timestamp = person_timestamp;
    }
}
