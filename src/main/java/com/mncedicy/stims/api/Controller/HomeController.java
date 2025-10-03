package com.mncedicy.stims.api.Controller;

import com.mncedicy.stims.api.Classes.Response;
import com.mncedicy.stims.api.Classes.RolePrivilegeData;
import com.mncedicy.stims.api.Classes.UserData;
import com.mncedicy.stims.api.Model.*;
import com.mncedicy.stims.api.Repo.*;
import com.mncedicy.stims.api.Services.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;


@SuppressWarnings("ALL")
//@CrossOrigin(origins = "http://localhost:59536")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("home")

public class HomeController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PersonRepo personRepo;
    @Autowired
    private RolePrivRepo rolePrivRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private PrivilegeRepo privilegeRepo;
    @Autowired
    private ContactRepo contactRepo;
    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private RuleReceiverRepo ruleReceiverRepo;


    @GetMapping(value = "/welcome")
    public String getPage(){
        return "Welcome";
    }

    @GetMapping(value = "/users")
    @ResponseBody
    public List<UserData> getUsers(@RequestParam int client_id){
        List<UserData> userDataList = new ArrayList<>();
        List<User> users= userRepo.findByClientId(client_id);
        for(User user : users){
            UserData userData = new UserData();
            userData.user = user;
            userData.person = personRepo.findById(user.user_person_id).get();
            userData.role = roleRepo.findById(user.user_role_id).get();
            userData.contact = contactRepo.findById(userData.person.person_contacts_id).get();
            userDataList.add(userData);
        }
        return userDataList;
    }

    @GetMapping(value = "/usersPerson")
    @ResponseBody
    public List<UserData> getUsersPerson(@RequestParam int client_id){
        List<UserData> userDataList = new ArrayList<>();
        List<User> users= userRepo.findByClientIdActive(client_id);
        for(User user : users){
            UserData userData = new UserData();
            userData.user = user;
            userData.user.setUser_password(null);
            userData.person = personRepo.findById(user.user_person_id).get();
            userDataList.add(userData);
        }
        return userDataList;
    }


    @GetMapping(value = "/usersLight")
    @ResponseBody
    public List<User> getUsersLight(@RequestParam int client_id){
        List<User> users = userRepo.findByClientId(client_id);
        for (User user : users) {
            user.setUser_password(null); // Call the setter method
        }
        return users;
    }

    @GetMapping(value = "/usersLightActive")
    @ResponseBody
    public List<User> getUsersLightActive(@RequestParam int client_id){
        List<User> users = userRepo.findByClientIdActive(client_id);
        for (User user : users) {
            user.setUser_password(null); // Call the setter method
        }
        return users;
    }


    @GetMapping(value = "/userByPersonId")
    @ResponseBody
    public UserData getUserByPersonId(@RequestParam long person_id){
        UserData userData = new UserData();
        userData.person = personRepo.findById(person_id).get();
        userData.contact = contactRepo.findById(userData.person.person_contacts_id).get();
        return userData;
    }


    @GetMapping(value = "/user/{id}")
    @ResponseBody
    public ResponseEntity<UserData> getUser(@PathVariable int id){
       User user= userRepo.findById(id).orElse(null);
        if(user != null){
            UserData userData = new UserData();
            userData.user = user;
            userData.user.setUser_password(null);
            userData.person = personRepo.findById(user.user_person_id).get();
            userData.role = roleRepo.findById(user.user_role_id).get();
            userData.contact = contactRepo.findById(userData.person.person_contacts_id).get();
           return ResponseEntity.ok(userData);
        }
        else{
           return ResponseEntity.notFound().build();
        }
    }


    @PostMapping(value = "/login")
    public Response loginUsers(@RequestBody User user){
        Response response = new Response();
        response.setStatus("Error");
        try {

         List<User> users= userRepo.findUserByUsername(user.user_username);
        if(!users.isEmpty()){
            if(passwordEncoder.matches(user.getUser_password(),users.get(0).getUser_password())) {
                if(users.get(0).user_status.equals("Active")){
                    UserData userData = new UserData();
                    userData.user = users.get(0);


                    userData.role = roleRepo.findById(userData.user.user_role_id).get();
                    userData.client = clientRepo.findById(userData.user.user_client_id).get();
                    userData.person = personRepo.findById(userData.user.user_person_id).get();
                    userData.contact = contactRepo.findById(userData.person.person_contacts_id).get();
                    userData.rolePrivileges = rolePrivRepo.findByRoleId(userData.user.user_role_id).stream().toList();

                    if(user.user_login_type.equals("Android") && userData.role.role_mobile_app.equals("No"))
                        response.setMessage("Not allowed to use mobile app");
                    else {
                        users.get(0).user_last_login = LocalDateTime.now();
                        users.get(0).user_login_type = user.user_login_type;
                        if(user.user_login_type.equals("Android"))
                            users.get(0).user_app_token = user.user_app_token;
                        else if(user.user_login_type.equals("Web"))
                            users.get(0).user_web_token = user.user_web_token;
                        userRepo.save(users.get(0));

                        userData.user.setUser_password("");
                        response.setData(userData);
                        response.setMessage("Successfully Authenticated");
                        response.setStatus("Success");
                    }
                }
                else {
                    response.setMessage("Your account is locked please contact administrator");
                }
            }
            else {
                response.setMessage("Incorrect Password");
            }
        }
        else {
            response.setMessage("Incorrect Username");
        }
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;

    }


    @GetMapping(value = "/changePassword")
    public Response changePassword(@RequestParam int user_id,@RequestParam String current_password,@RequestParam String new_password){
        Response response = new Response();
        response.setStatus("Error");
        try {

            User user= userRepo.findById(user_id).get();
            if(current_password.isEmpty() || passwordEncoder.matches(current_password,user.getUser_password())) {
                user.setUser_password(passwordEncoder.encode(new_password));
                user.user_password_type = "Permanent";
                user = userRepo.save(user);response.setData(user);
                response.setMessage("Successfully changed");
                response.setStatus("Success");
            }
            else {
                response.setMessage("Your current password is incorrect");
            }
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;

    }


    @GetMapping(value = "/forgotPassword")
    public Response forgotPassword(@RequestParam String user_username){
        Response response = new Response();
        response.setStatus("Error");
        try {
            EmailServiceImpl emailService = new EmailServiceImpl();
            List<User> users= userRepo.findUserByUsername(user_username);
            if(!users.isEmpty()) {
                if(users.get(0).user_status.equals("Active")){
                    int code = new Random().nextInt(9999-1000 +1) + 1000;
                    String text = "Hi\n\n"+
                            "To verify your account, please enter the following code " +
                            "on the verification page: "+code+" \n\nRegards";
                    emailService.sendSimpleMessage(users.get(0).user_username,"OTP Verification Code",text);
                    response.setMessage("Verification code is sent to your email");
                    response.setStatus("Success");
                    response.setExtra(""+code);
                    response.setId((long)users.get(0).user_id);
                }
                else {
                    response.setMessage("Your account is locked please contact administrator");
                }
            }
            else {
                response.setMessage("Username/Email address not found");
            }
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;

    }


    @GetMapping(value = "/roles")
    @ResponseBody
    public  List<RolePrivilegeData> roles(@RequestParam int client_id){
        List<RolePrivilegeData> rolePrivilegeDataList = new ArrayList<>();
        List<Role> roles= roleRepo.findByClientId(client_id);
        for(Role role : roles){
            RolePrivilegeData rolePrivilegeData = new RolePrivilegeData();
            rolePrivilegeData.role = role;
            rolePrivilegeData.myPrivileges = rolePrivRepo.findByRoleId(role.role_id).stream().toList();
            rolePrivilegeDataList.add(rolePrivilegeData);
        }
        return rolePrivilegeDataList;
    }

    @GetMapping(value = "/activeRoles")
    @ResponseBody
    public  List<Role> activeRoles(@RequestParam int client_id){
        List<Role> roles= roleRepo.findActiveByClientId(client_id);
        return roles;
    }



    @GetMapping(value = "/getPrivileges")
    @ResponseBody
    public  List<Privilege> getPrivileges(){
        List<Privilege> privileges= privilegeRepo.findAll();
        return privileges;
    }


    @GetMapping(value = "/getUsersByRoleId")
    @ResponseBody
    public  List<UserData> getUsersByRoleId(@RequestParam int client_id,@RequestParam int role_id){
        List<User> users= userRepo.findByClientIdAndRole(client_id,role_id);
        List<UserData> userDataList = new ArrayList<>();
        for(User user : users){
            UserData userData = new UserData();
            userData.user = user;
            userData.user.setUser_password("");
            userData.role = roleRepo.findById(userData.user.user_role_id).get();
            userData.client = clientRepo.findById(userData.user.user_client_id).get();
            userData.person = personRepo.findById(userData.user.user_person_id).get();
            userData.contact = contactRepo.findById(userData.person.person_contacts_id).get();
            userData.rolePrivileges = rolePrivRepo.findByRoleId(userData.user.user_id).stream().toList();
            userDataList.add(userData);
        }
        return userDataList;
    }


    @PostMapping(value = "/saveRole")
    public Response saveRole(@RequestBody RolePrivilegeData data){
        Response response = new Response();
        response.setStatus("Error");
        try {
            List<Role> roles = roleRepo.findById(data.role.role_id).stream().toList();
            if(!roles.isEmpty()){
                if(data.role.role_status.equals(roles.get(0).role_status)) {
                    for (RolePrivilege item : data.myPrivileges) {
                        item.role_privilege_role_id = data.role.role_id;
                    }
                    rolePrivRepo.deleteByRoleId(data.role.role_id);
                    data.myPrivileges = rolePrivRepo.saveAll(data.myPrivileges);
                }
                roles.get(0).role_last_update = LocalDateTime.now();
                roles.get(0).role_status = data.role.role_status;
                roles.get(0).role_description = data.role.role_description;
                roles.get(0).role_name = data.role.role_name;
                roles.get(0).role_landing_page = data.role.role_landing_page;
                roles.get(0).role_target = data.role.role_target;
                roles.get(0).role_category = data.role.role_category;
                data.role = roleRepo.save(roles.get(0));
            }
            else{
                List<Role> roles1 = roleRepo.findByClientIdAndName(data.role.role_client_id,data.role.role_name);
                if(!roles1.isEmpty()){
                    response.setMessage("Role: "+data.role.role_name+" already exist");
                    return response;
                }

                data.role= roleRepo.save(data.role);
                for (RolePrivilege item : data.myPrivileges) {
                    item.role_privilege_role_id = data.role.role_id;
                }
                data.myPrivileges = rolePrivRepo.saveAll(data.myPrivileges);
            }

            response.setData(data.myPrivileges);
            response.setMessage("Successfully Saved");
            response.setStatus("Success");
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;

    }




    @PostMapping(value = "/saveUser")
    public Response saveUser(@RequestBody UserData data){
        Response response = new Response();
        EmailServiceImpl emailService = new EmailServiceImpl();
        response.setStatus("Error");
        try {
            List<User> users = userRepo.findById(data.user.user_id).stream().toList();
            User user = new User();
            Person person = new Person();
            Contact contact = new Contact();
            if(!users.isEmpty()){
                user= users.get(0);
                person = personRepo.findById(data.person.person_id).get();
                contact = contactRepo.findById(data.contact.contact_id).get();
                ruleReceiverRepo.updateUserStatusName(data.person.person_id,data.user.user_status,data.person.person_first_name+ " "+data.person.person_last_name);
            }
            else {
                List<User> users1= userRepo.findUserByUsername(data.user.user_username);
                if(!users1.isEmpty()){
                    response.setMessage("Email address: "+data.user.user_username+" already registered");
                    return response;
                }
                List<Person> people= personRepo.findByIdNumber(data.person.person_id_number);
                if(!people.isEmpty()){
                    response.setMessage("ID number: "+data.person.person_id_number+" already registered");
                    return response;
                }
                Random r = new Random();
                int pass = r.nextInt(999999-100000 +1) + 100000;
                user.setUser_password(passwordEncoder.encode((pass+"")));
                String text = "Hi "+data.person.person_first_name+"\n\n"+
                        "Make use of a temporary password, log into the system or application " +
                        "with the provided temporary password. Once logged in, you should be " +
                        "prompted to change the password to a more secure one.\n\n"+
                        "Temporary password: "+pass+" \n\nRegards";
                emailService.sendSimpleMessage(data.user.user_username,"Temporary Password",text);

            }

            contact.contact_type = data.contact.contact_type;
            contact.contact_last_update = LocalDateTime.now();
            contact.contact_cellphone = data.contact.contact_cellphone;
            contact.contact_email_address = data.contact.contact_email_address;
            contact.contact_physical_address_street = data.contact.contact_physical_address_street;
            contact.contact_physical_address_suburb = data.contact.contact_physical_address_suburb;
            contact.contact_physical_address_city = data.contact.contact_physical_address_city;
            contact.contact_physical_address_code = data.contact.contact_physical_address_code;
            contact.contact_postal_address_street = data.contact.contact_postal_address_street;
            contact.contact_postal_address_suburb = data.contact.contact_postal_address_suburb;
            contact.contact_postal_address_city = data.contact.contact_postal_address_city;
            contact.contact_postal_address_code = data.contact.contact_postal_address_code;
            data.contact = contactRepo.save(contact);

            person.person_contacts_id = data.contact.contact_id;
            person.person_client_id = data.person.person_client_id;
            person.person_title = data.person.person_title;
            person.person_first_name = data.person.person_first_name;
            person.person_last_name = data.person.person_last_name;
            person.person_id_number = data.person.person_id_number;
            data.person = personRepo.save(person);


            user.user_person_name = data.person.person_first_name+ " "+data.person.person_last_name;
            user.user_person_id = data.person.person_id;
            user.user_last_update = LocalDateTime.now();
            user.user_status = data.user.user_status;
            user.user_username = data.user.user_username;
            user.user_role_name = data.user.user_role_name;
            user.user_role_id = data.user.user_role_id;
            user.user_officer_inspector_number = data.user.user_officer_inspector_number;
            user.user_client_id = data.user.user_client_id;
            data.user = userRepo.save(user);



            response.setData(data);
            response.setMessage("Successfully Saved");
            response.setStatus("Success");
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;

    }



    @PostMapping(value = "/savePrivilege")
    public Response savePrivilege(@RequestBody Privilege privilege){
        Response response = new Response();
        response.setStatus("Error");
        try {

            privilege = privilegeRepo.save(privilege);


            response.setData(privilege);
            response.setMessage("Successfully Saved");
            response.setStatus("Success");
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;

    }




}
