$(document).ready(function () {
   
    var client;
 
    getClient();

    function getClient() {
        showLoader();
        var details = {
            client_id: client_id
        }
        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/configuration/getClient",  //the url to call
            contentType: "application/json",
            data: jQuery.param(details)
        }).done(function (data) {
            console.log(data);
            client = data;
            loadClient();


            hideLoader();
        }).fail(function (err) {
            console.log(err.statusText);
            hideLoader();
            showMessage('danger', 'Error', err.statusText, 5000);

        });
    }

    function loadClient() {
        $('#txtClientName').val(client.client.client_name);
        $('#txtEmailAddress').val(client.client.client_email);
        $('#txtTelephone').val(client.client.client_phone);
        $('#txtWebsite').val(client.client.client_website);
        $('#txtLogoLink').val(client.client.client_logo);
        $('#txtAuthorityCode').val(client.client.client_authority_code);
        $('#txtFaxNumber').val(client.client.client_fax);
        $('#txtOpeningHour').val(getTimee(client.client.client_hour_opening));
        $('#txtClosingHour').val(getTimee(client.client.client_hour_closing));

        $('#txtPhysicalAddress').val(client.contact.contact_physical_address_street);
        $('#txtPhysicalSuburb').val(client.contact.contact_physical_address_suburb);
        $('#txtPhysicalCity').val(client.contact.contact_physical_address_city);
        $('#txtPhysicalCode').val(client.contact.contact_physical_address_code);
        $('#txtPostalAddress').val(client.contact.contact_postal_address_street);
        $('#txtPostalSuburb').val(client.contact.contact_postal_address_suburb);
        $('#txtPostalCity').val(client.contact.contact_postal_address_city);
        $('#txtPostalCode').val(client.contact.contact_postal_address_code);

        $('#txtAccountName').val(client.client.client_bank_account_name);
        $('#txtBankName').val(client.client.client_bank_name);
        $('#txtAccountNumber').val(client.client.client_bank_account_number);
        $('#txtBranchCode').val(client.client.client_bank_branch_code);
        $('#txtCourtesyLetterFee').val(client.client.client_courtesy_amount);
        $('#txtEnforcementOrderFee').val(client.client.client_enforcement_amount);
        $('#txtWarrantFee').val(client.client.client_warrant_amount);
        $('#cboDiscount').val(client.client.client_discount);

        $('#txtTelephoneAlt').val(client.client.client_alt_phone);
        $('#txtDiscountPercentage').val(client.client.client_discount_percentage);
        $('#txtDiscountTimeframe').val(client.client.client_discount_timeframe);
        $('#txtCourtesyLetter').val(client.client.client_courtesy);
        $('#txtCourtesyLetterTimeframe').val(client.client.client_courtesy_timeframe);
        $('#txtEnforcementOrder').val(client.client.client_enforcement);
        $('#txtEnforcementOrderTimeframe').val(client.client.client_enforcement_timeframe);
        $('#txtWarrant').val(client.client.client_warrant);
        $('#txtWarrantTimeframe').val(client.client.client_warrant_timeframe);



        if (myPrivs.indexOf(41) < 0) {
            $('.clientField').attr('disabled', 'disabled');
            $('#btnSaveChanges').addClass('hidden');
        }
        else {
            validatePhoneEmail('.txtEmail','Email Address');
            validatePhoneEmail('#txtTelephone', 'Phone Number');
            validatePhoneEmailOrEmpty('#txtTelephoneAlt', 'Alternative Number');
            validatePhoneEmailOrEmpty('#txtFaxNumber', 'Fax Number');
            validateFields1('requiredClient1');
            validateFields1('requiredClient');
            validateFields1('requiredClient2');
            validateFieldsMinNumber('.requiredMinNumber',1);
        }

        $('.requiredMin').trigger('change');
    }

    $('.requiredMin').on('change', function (evt) {
        if ($(this).val() == "Yes") 
            $(this).parent('div').parent('div').find('input').addClass('requiredMinNumber').parent('div').show('fast');
        else
            $(this).parent('div').parent('div').find('input').val('0').removeClass('requiredMinNumber').parent('div').hide('fast');
    });




    $('.clientField').on('change', function (evt) {
        $('#btnSaveChanges').show('fast');
    }).on('keydown', function (evt) {
        $('#btnSaveChanges').show('fast');
    });

    $('#btnSaveChanges').on('click', function (evt) {
       
        if (!validatePhoneEmail('.txtEmail', 'Email Address') || !validatePhoneEmail('#txtTelephone', 'Phone Number') || 
            !validatePhoneEmailOrEmpty('#txtTelephoneAlt', 'Alternative Number') || !validatePhoneEmailOrEmpty('#txtFaxNumber', 'Fax Number') ||
            validateFields1('requiredClient1')) {
            $('#step1')[0].click();
            return;
        } 
        else if (validateFields1('requiredClient')) {
            $('#step2')[0].click();
            return;
        } 
        else if (validateFields1('requiredClient2') || validateFieldsMinNumber('.requiredMinNumber', 1)) {
            $('#step3')[0].click();
            return;
        } 

        prepareClient();       
    });

    function prepareClient() {
        client.client.client_name =  $('#txtClientName').val();
        client.client.client_email =  $('#txtEmailAddress').val();
        client.client.client_phone =  $('#txtTelephone').val();
        client.client.client_website =  $('#txtWebsite').val();
        client.client.client_logo =  $('#txtLogoLink').val();
        client.client.client_authority_code =  $('#txtAuthorityCode').val();
        client.client.client_fax = $('#txtFaxNumber').val();
        client.client.client_hour_opening = $('#txtOpeningHour').val();
        client.client.client_hour_closing = $('#txtClosingHour').val();

        client.client.client_updated_by = person_id;
        client.client.client_updated_by_name = person_name;
        client.contact.contact_email_address = client.client.client_email;
        client.contact.contact_telephone = client.client.client_phone;
        client.contact.contact_website = client.client.client_website;

        client.contact.contact_physical_address_street =  $('#txtPhysicalAddress').val();
        client.contact.contact_physical_address_suburb =  $('#txtPhysicalSuburb').val();
        client.contact.contact_physical_address_city =  $('#txtPhysicalCity').val();
        client.contact.contact_physical_address_code =  $('#txtPhysicalCode').val();
        client.contact.contact_postal_address_street =  $('#txtPostalAddress').val();
        client.contact.contact_postal_address_suburb =  $('#txtPostalSuburb').val();
        client.contact.contact_postal_address_city =  $('#txtPostalCity').val();
        client.contact.contact_postal_address_code = $('#txtPostalCode').val();

        client.client.client_bank_account_name =  $('#txtAccountName').val();
        client.client.client_bank_name =  $('#txtBankName').val();
        client.client.client_bank_account_number =  $('#txtAccountNumber').val();
        client.client.client_bank_branch_code =  $('#txtBranchCode').val();
        client.client.client_courtesy_amount =  $('#txtCourtesyLetterFee').val();
        client.client.client_enforcement_amount =  $('#txtEnforcementOrderFee').val();
        client.client.client_warrant_amount =  $('#txtWarrantFee').val();
        client.client.client_discount = $('#cboDiscount').val();

        client.client.client_alt_phone = $('#txtTelephoneAlt').val();
        client.client.client_discount_percentage = $('#txtDiscountPercentage').val();
        client.client.client_discount_timeframe = $('#txtDiscountTimeframe').val();
        client.client.client_courtesy = $('#txtCourtesyLetter').val();
        client.client.client_courtesy_timeframe = $('#txtCourtesyLetterTimeframe').val();
        client.client.client_enforcement = $('#txtEnforcementOrder').val();
        client.client.client_enforcement_timeframe = $('#txtEnforcementOrderTimeframe').val();
        client.client.client_warrant = $('#txtWarrant').val();
        client.client.client_warrant_timeframe = $('#txtWarrantTimeframe').val();

        saveClient();


    }

    function saveClient() {



        console.log(client);

       
        showLoader();

        $.ajax({
            type: "POST", //GET, POST, PUT
            url: httpsapi + "/configuration/saveClient",  //the url to call
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(client),
            success: function (data, text) {
                hideLoader();
                console.log(data);
                if (data.status == 'Success') {
                    showMessage('success', 'success', data.message, 3000);
                    $('#btnSaveChanges').hide('fast');
                }
                else {
                    
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

});