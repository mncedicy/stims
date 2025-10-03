$(document).ready(function () {

    checkPriv(14);
   
    var selectedUser;
    var users = [];
    var user = {};
    var person = {};
    var contact = {};
    var roles = [];

    if (myPrivs.indexOf(1) < 0)
        $('#btnAddNew').addClass('hidden');
    var showEdit = myPrivs.indexOf(2) < 0 ? 'hidden' : '';
    var showDelete = myPrivs.indexOf(3) < 0 ? 'hidden' : '';
    var showReactivate = myPrivs.indexOf(46) < 0 ? 'hidden' : '';




    usersLight();
    function usersLight() {
        showLoader();

        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/home/usersLight?client_id=" + client_id,  //the url to call
            contentType: "application/json"
        }).done(function (data) {
            users = data;
            console.log(data);
            activeRoles();
           var str = '';
            for (i = 0; i < data.length; i++) {
                var status = getStatus(data[i].user_status);
                var deleted = status.status == "Deleted" ? 'hidden="hidden"' : '';
                var undeleted = status.status != "Deleted" ? 'hidden="hidden"' : '';

                str += '<tr><td>' + (i + 1) + '</td><td>' + data[i].user_username + '</td>';
                str += '<td class="text-capitalize">' + data[i].user_person_name + '</td>';
                str += '<td class="text-capitalize">' + data[i].user_role_name + '</td>';
                str += '<td class="text-capitalize">' + getDatee(data[i].user_last_login) + '</td>';
                str += '<td>' + status.span + '</td>';
                str += '<td class="text-center clsAction"><a  ' + deleted + ' class="clsEdit ' + showEdit +'" id="c' + i + '" href="#!"><i class="icon feather icon-edit tblIcon text-primary"></i>';
                str += '</a><a href="#!" ' + deleted + ' class="clsDelete ' + showDelete +'" id="d' + i + '"><i class="feather icon-trash-2 tblIcon text-danger"></i></a>';
                str += '<a href="#!" ' + undeleted + '  class="clsActivate ' + showReactivate +'" id="a' + i + '"><i class="feather icon-rotate-ccw tblIcon text-success"></i></a></td></tr>';
           }
            $('#tblUser').html(str);

            if (showEdit && showDelete && showReactivate)
                $('.clsAction').hide();

            $('.clsDelete').click(function () {
                selectedUser = parseInt(this.id.substr(1));
                showConfirm('danger', 'Confirmation', 'Are you sure you want to delete this user?');
            });

            $('.clsActivate').click(function () {
                selectedUser = parseInt(this.id.substr(1));
                showConfirm('success', 'Confirmation', 'Are you sure you want to re-activate this user?');
            });

            $('.clsEdit').click(function () {
                selectedUser = parseInt(this.id.substr(1));
                user = users[selectedUser];
                userByPersonId(user.user_person_id);
            });



            hideLoader();
        }).fail(function (err) {
            console.log(err);
        });
    }


  function userByPersonId(person_id) {
        showLoader();

        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/home/userByPersonId?person_id=" + person_id,  //the url to call
            contentType: "application/json"
        }).done(function (data) {
            console.log(data);
            $('#addUserModal').modal('show');
            $('#step1').trigger('click');
            contact = data.contact;
            person = data.person;
            loadAll();

            hideLoader();
        }).fail(function (err) {
            console.log(err);
        });
    }




    function loadAll() {
         $('#txtEmailAddress').val(user.user_username);
         $('#cboRoleName').val(user.user_role_id);
         $('#txtInspectorNumber').val(user.user_officer_inspector_number);

         $('#cboTitle').val(person.person_title);
         $('#txtFirstName').val(person.person_first_name);
        $('#txtLastName').val(person.person_last_name);
        $('#txtIDNumber').val(person.person_id_number);

         $('#txtCellphoneNumber').val(contact.contact_cellphone);
         $('#txtEmailAddress').val(contact.contact_email_address);
         $('#txtPhysicalAddress').val(contact.contact_physical_address_street);
         $('#txtPhysicalSuburb').val(contact.contact_physical_address_suburb);
         $('#txtPhysicalCity').val(contact.contact_physical_address_city);
         $('#txtPhysicalCode').val(contact.contact_physical_address_code);
         $('#txtPostalAddress').val(contact.contact_postal_address_street);
         $('#txtPostalSuburb').val(contact.contact_postal_address_suburb);
         $('#txtPostalCity').val(contact.contact_postal_address_city);
         $('#txtPostalCode').val(contact.contact_postal_address_code);
        $('.triggers').trigger('focusout').attr('disabled', 'disabled');
    }


    $('#btnAddNew').click(function () {
        $('#addUserModal').modal('show');
        $('.clsUser').val('');
        $('#step1').trigger('click');
        $('#txtInspectorNumber').closest('div').hide('fast');
        $('.triggers').removeAttr('disabled');
        user = {}; contact = {}; person = {};
    });
    $('#cboRoleName').change(function () {
        $('#txtInspectorNumber').val('');
        if ($("#cboRoleName option:selected").text() == 'Traffic Officer')
            $('#txtInspectorNumber').closest('div').show('fast');
        else
            $('#txtInspectorNumber').closest('div').hide('fast');
    });


    activeRoles();
    function activeRoles() {

        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/home/activeRoles?client_id=" + client_id,  //the url to call
            contentType: "application/json"
        }).done(function (data) {
            roles = data;
            console.log(data);
            var str = '<option disabled="disabled" selected value="">Select</option>';
            for (var i = 0; i < data.length; i++) {
                str += '<option value="' + data[i].role_id + '">' + data[i].role_name + '</option>';
            }
            $('#cboRoleName').html(str);

        }).fail(function (err) {
            console.log(err);
            showMessage('danger', 'Error', err, 5000);
        });
    }




    $('#btnSubmitConfirm').click(function () {

        $('#confirmModal').modal('hide');
        users[selectedUser].user.user_status = users[selectedUser].user.user_status == "Deleted" ? "Active" : "Deleted";
        saveUser(users[selectedUser]);
    });












    $('#txtIDNumber').focusout(function () {
        person.person_id_number = null;
        if (validateIdNumber($(this)).valid)
            person.person_id_number = $(this).val();
    });
    $('#txtIDNumber').focusin(function () {
        $(this).parent('div').find('label').text('ID Number');
    });

    $('#txtEmailAddress').focusout(function () {
        user.user_username = null;
        if (validateEmailAddress($(this)))
            user.user_username = $(this).val();
    });
    $('#txtEmailAddress').focusin(function () {
        $(this).parent('div').find('label').text('Email Address');
    });



    $('.requiredUser1').focusout(function () {
        validateFieldsOne($(this));
    });
    $('.requiredUser1').focusin(function () {
    });

    
    $('#btnSubmitUser').click(function () {

      

        if ($(this).text() == 'Save User') {
            $('.triggers').trigger('focusout');
            if (!user.user_username || !person.person_id_number || validateFields1('requiredUser1')) {
                $('#step1').trigger('click');
                return;
            }
  

            user.user_status = "Active";
            user.user_username = $('#txtEmailAddress').val();
            user.user_role_name = $("#cboRoleName option:selected").text();
            user.user_role_id = $('#cboRoleName').val();
            user.user_officer_inspector_number = $('#txtInspectorNumber').val();
            user.user_client_id = client_id;

            person.person_client_id = client_id;
            person.person_title = $('#cboTitle').val();
            person.person_first_name = $('#txtFirstName').val();
            person.person_last_name = $('#txtLastName').val();

            contact.contact_type = "Person";
            contact.contact_cellphone = $('#txtCellphoneNumber').val();
            contact.contact_email_address = $('#txtEmailAddress').val();
            contact.contact_physical_address_street = $('#txtPhysicalAddress').val();
            contact.contact_physical_address_suburb = $('#txtPhysicalSuburb').val();
            contact.contact_physical_address_city = $('#txtPhysicalCity').val();
            contact.contact_physical_address_code = $('#txtPhysicalCode').val();
            contact.contact_postal_address_street = $('#txtPostalAddress').val();
            contact.contact_postal_address_suburb = $('#txtPostalSuburb').val();
            contact.contact_postal_address_city = $('#txtPostalCity').val();
            contact.contact_postal_address_code = $('#txtPostalCode').val();



            var details = {
                user: user,
                person: person,
                contact: contact
            };

            saveUser(details);
        }
    });


    function saveUser(details) {



        console.log(details, httpsapi + "/home/saveUser", JSON.stringify(details));
    

        showLoader();

        $.ajax({
            type: "POST", //GET, POST, PUT
            url: httpsapi + "/home/saveUser",  //the url to call
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(details),
            success: function (data, text) {
                hideLoader();
                console.log(data);
                if (data.status == 'Success') {
                    usersLight();
                    $('#addUserModal').modal('hide');
                    showMessage('success', 'success', data.message, 3000);
                }
                else {
                    $('#step1').trigger('click');
                    showMessage('danger', 'Error', data.message, 5000);
                }
            },
            error: function (request, status, error) {
                console.log(request, status, error);
                hideLoader();
                showMessage('danger', 'Error', error, 5000);
            }

        });


    }





















    var navListItems = $('div.setup-panel div a'),
        allWells = $('.setup-content'),
        allNextBtn = $('.nextBtn');

    allWells.hide();

    navListItems.click(function (e) {
        e.preventDefault();
        var $target = $($(this).attr('href')),
            $item = $(this);

        if (!$item.hasClass('disabled')) {
            navListItems.removeClass('btn-primary').addClass('btn-light');
            navListItems.parent("div").find('small').removeClass('text-primary').removeClass('activeTab');
            $item.addClass('btn-primary');
            $item.parent("div").find('small').addClass('text-primary').addClass('activeTab');
            allWells.hide();
            $target.show();
            $target.find('input:eq(0)').focus();
            setTimeout(function () {
                if (parseInt($item.text()) == navListItems.length)
                    allNextBtn.text('Save User');
                else
                    allNextBtn.text('Next');
            }, 10);




        }
    });

    allNextBtn.click(function () {
        console.log($(this).text());
        if ($(this).text() == 'Next') {
            nextStepWizard = $('div.setup-panel div a.btn-primary').parent().next().children("a");
            nextStepWizard.trigger('click');
        }
    });

    $('div.setup-panel div a.btn-primary').trigger('click');







});