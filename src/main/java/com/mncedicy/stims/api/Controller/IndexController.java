package com.mncedicy.stims.api.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String Index(Model model){
        model.addAttribute("view","view/Login/Index");
        return "view/layout";
    }




    @RequestMapping("/Home")
    public String Home(Model model){
        model.addAttribute("view","view/Home/Index");
        return "view/layout";
    }

    @RequestMapping("/Home/Roles")
    public String Roles(Model model){
        model.addAttribute("view","view/Home/Roles");
        return "view/layout";
    }

    @RequestMapping("/Home/Users")
    public String Users(Model model){
        model.addAttribute("view","view/Home/Users");
        return "view/layout";
    }




    @RequestMapping("/Capturing/Book")
    public String Book(Model model){
        model.addAttribute("view","view/Capturing/Book");
        return "view/layout";
    }
    @RequestMapping("/Capturing/Enatis")
    public String Enatis(Model model){
        model.addAttribute("view","view/Capturing/Enatis");
        return "view/layout";
    }
    @RequestMapping("/Capturing/Notice")
    public String Notice(Model model){
        model.addAttribute("view","view/Capturing/Notice");
        return "view/layout";
    }
    @RequestMapping("/Capturing/Verification")
    public String Verification(Model model){
        model.addAttribute("view","view/Capturing/Verification");
        return "view/layout";
    }




    @RequestMapping("/Configuration/Advanced")
    public String Advanced(Model model){
        model.addAttribute("view","view/Configuration/Advanced");
        return "view/layout";
    }

    @RequestMapping("/Configuration/ChargeCodes")
    public String ChargeCodes(Model model){
        model.addAttribute("view","view/Configuration/ChargeCodes");
        return "view/layout";
    }

    @RequestMapping("/Configuration/LocationCodes")
    public String LocationCodes(Model model){
        model.addAttribute("view","view/Configuration/LocationCodes");
        return "view/layout";
    }

    @RequestMapping("/Configuration/Automations")
    public String Automations(Model model){
        model.addAttribute("view","view/Configuration/Automations");
        return "view/layout";
    }





    @RequestMapping("/Management/Infringements")
    public String Infringements(Model model){
        model.addAttribute("view","view/Management/Infringements");
        return "view/layout";
    }

    @RequestMapping("/Management/Payments")
    public String Payments(Model model){
        model.addAttribute("view","view/Management/Payments");
        return "view/layout";
    }

    @RequestMapping("/Management/Officer")
    public String Officer(Model model){
        model.addAttribute("view","view/Management/Officer");
        return "view/layout";
    }




    @RequestMapping("/Printing/CourtRoll")
    public String CourtRoll(Model model){
        model.addAttribute("view","view/Printing/CourtRoll");
        return "view/layout";
    }

    @RequestMapping("/Printing/Dashboards")
    public String Dashboards(Model model){
        model.addAttribute("view","view/Printing/Dashboards");
        return "view/layout";
    }

    @RequestMapping("/Printing/Legal")
    public String Legal(Model model){
        model.addAttribute("view","view/Printing/Legal");
        return "view/layout";
    }

    @RequestMapping("/Printing/Reports")
    public String Reports(Model model){
        model.addAttribute("view","view/Printing/Reports");
        return "view/layout";
    }

    @RequestMapping("/payment/success")
    public String paymentSuccess(Model model){
        model.addAttribute("type","success");
        return "view/Payment/Results";
    }
    @RequestMapping("/payment/failure")
    public String paymentFailure(Model model){
        model.addAttribute("type","failure");
        return "view/Payment/Results";
    }
    @RequestMapping("/payment/cancel")
    public String paymentCancel(Model model){
        model.addAttribute("type","cancel");
        return "view/Payment/Results";
    }



}
