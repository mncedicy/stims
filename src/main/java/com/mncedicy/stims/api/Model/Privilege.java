package com.mncedicy.stims.api.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long privilege_id;
    public String privilege_name;
    public String privilege_description;
    public String privilege_type;
    public String privilege_subtype;
    public String privilege_path;
}
