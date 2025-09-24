package com.mncedicy.stims.api.Classes;

import com.mncedicy.stims.api.Model.*;
import com.mncedicy.stims.api.Repo.*;
import com.mncedicy.stims.api.Repo.EnatisFileRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EnatisData {

    public List<enatis_make> makes;
    public List<enatis_model> models;
    public List<enatis_colour> colours;
    public List<enatis_type> types;
    public List<enatis_usage> usages;


}
