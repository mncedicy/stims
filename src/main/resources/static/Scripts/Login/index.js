$(document).ready(function () {

  

    //--------------------- Today's date ------------------------//


    var today = new Date();

    var dd = today.getDate();
    var mm = today.getMonth() + 1;
    var yyyy = today.getFullYear();

    if (mm < 10)
        mm = "0" + mm;
    if (dd < 10)
        dd = "0" + dd;

    var todayDate = yyyy + "-" + mm + "-" + dd;


    //--------------------- Today's date ------------------------//


  

    deleteAllCookies();


    $('#btnSignIn').on('click', function (evt) {

        signin(evt);
        evt.preventDefault();
    });



    $(document).on('keyup', function (evt) {

        if (evt.which == 13) {
            signin(evt);
        }
        //evt.preventDefault();
    });

    

    function signin(evt) {

      if (validateFields1('requiredLogin'))
            return;

        showLoader();



        var details = {
            user_username: $('#txtUsername').val(),
            user_password: $('#txtPassword').val(),
            user_login_type : "Api",
            user_web_token: 'token'
        };

       // console.log(details);

                $.ajax({
                    type: "POST", //GET, POST, PUT
                    url: "/home/login",  //the url to call
                    contentType: "application/json",
                    dataType: "json",
                    data: JSON.stringify(details),
                    success: function (data, text) {


                              // console.log(data);
                                   $('#loaderModal').modal('hide');


                                   if (data.status == 'Success') {
                                       var d1 = new Date();
                                       setCookie('login_time', d1.getTime(), 999);
                                       setCookie('client_id', data.data.person.person_client_id, 999);
                                       setCookie('role_name', data.data.user.user_role_name, 999);
                                       setCookie('user_id', data.data.user.user_id, 999);
                                        setCookie('user_status', data.data.user.user_status, 999);
                                       setCookie('person_full_name', data.data.person.person_first_name + ' ' + data.data.person.person_last_name, 999);
                                       setCookie('person_name', data.data.person.person_first_name, 999);
                                       setCookie('person_id', data.data.person.person_id, 999);
                                       setCookie('person_surname', data.data.person.person_last_name, 999);
                                       setCookie('user_username', data.data.user.user_username, 999);
                                       setCookie('client_authority_code', data.data.client.client_authority_code, 999);
                                       setCookie('client_logo', data.data.client.client_logo, 999);
                                       setCookie('client_name', data.data.client.client_name, 999);
                                       setCookie('client_status', data.data.client.client_status, 999);
                                       setCookie('role_landing_page', data.data.role.role_landing_page, 999);
                                       setCookie('user_password_type', data.data.user.user_password_type, 999);
                                       setCookie('parameter', '', 0);
                                        setCookie('contact_cellphone', data.data.contact.contact_cellphone, 999);
                                        setCookie('contact_address', data.data.contact.contact_physical_address_street, 999);
                                        setCookie('login_date', getNow(), 999);


                                       myPrivs = Array();
                                       myPrivsPath = Array();
                                       for (var i = 0; i < data.data.rolePrivileges.length; i++) {
                                           myPrivs.push(data.data.rolePrivileges[i].role_privilege_privilege_id);
                                           myPrivsPath.push(data.data.rolePrivileges[i].role_privilege_path);
                                       }
                                       setCookie('myPrivs', JSON.stringify(myPrivs), 999);
                                       setCookie('myPrivsPath', JSON.stringify(myPrivsPath), 999);
                                       showMessage('success','success', data.message, 3000);
                                       location.href = data.data.role.role_landing_page;

                                   }
                                   else {
                                       showMessage('danger','access denied', data.message,3000);
                                   }

                                   hideLoader();


                    },
                    error: function (request, status, error) {
                        console.log(request, status, error);
                        hideLoader();
                        showMessage('danger', 'Error', request.responseText, 5000);
                    }

                });




    }















    var email = '';
    var otp = '';
    var user_id;

    $('#btnForgot').on('click', function (evt) {
        $(".requiredEmail").val('');
        $("#emailModal").modal('show');
    });

    $('#btnSubmitEmail').on('click', function (evt) {
        email = $('#txtEmail').val();
        if (validateEmailAddress($('#txtEmail')))
            forgotPassword();
    });

    $('#btnResendOTP').on('click', function (evt) {
            forgotPassword();
    });



    function forgotPassword() {
        showLoader();
        
        var details = {
            user_username: email
        }
  
        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/home/forgotPassword",  //the url to call
            contentType: "application/json",
            data: jQuery.param(details)
        }).done(function (data) {
          //  console.log(data);
            if (data.status == 'Success') {
                $("#emailModal").modal('hide');
                $('#otpModal').modal('show');
                otp = data.extra;
                user_id = data.id;
                showMessage('success', 'success', data.message, 3000);
            }
            else {
                showMessage('danger', 'Error', data.message, 5000);
            }

            hideLoader();
        }).fail(function (err) {
            console.log(err.statusText);
            hideLoader();
            showMessage('danger', 'Error', err.statusText, 5000);

        });
    }



    $('#btnSubmitOTP').on('click', function (evt) {
        if (validateFieldsLeg('requiredOTP', 4))
            return;

        if (otp != $('#txtOTP').val())
        {
            showMessage('danger', 'Error', 'The one-time password you entered is not valid', 4000);
            return;
        }
        $(".requiredPassword").val('');
        $("#txtCurrentPassword").removeClass('requiredPassword');
        $(".changeElem").hide();
        $('#otpModal').modal('hide');
        $("#passwordModal").modal('show');
    });


    $("#btnSubmitPassword").click(function () {
        if (validateFieldsLeg('requiredPassword', 8))
            return;
        if ($("#txtNewPassword").val() != $("#txtConfirmPassword").val()) {
            showMessage('danger', 'Error', 'New password and confirm password does not match', 4000);
            $(".requiredPassword").val('');
            $("#txtNewPassword").focus();
            return;
        }

        changePassword();
    });

    function changePassword() {
        showLoader();
        var details = {
            user_id: user_id,
            current_password: $("#txtCurrentPassword").val(),
            new_password: $("#txtNewPassword").val()
        }
        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/home/changePassword",  //the url to call
            contentType: "application/json",
            data: jQuery.param(details)
        }).done(function (data) {
          //  console.log(data);
            if (data.status == 'Success') {
                $('#passwordModal').modal('hide');
                showMessage('success', 'success', data.message, 3000);
            }
            else {
                showMessage('danger', 'Error', data.message, 5000);
            }

            hideLoader();
        }).fail(function (err) {
            console.log(err.statusText);
            hideLoader();
            showMessage('danger', 'Error', err.statusText, 5000);

        });
    }























});