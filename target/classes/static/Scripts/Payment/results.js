$(document).ready(function () {

       var pathSegments = window.location.pathname.split('/');
        var uuids = [pathSegments[pathSegments.length-1]];

    console.log(uuids);

    checkout();

    function checkout() {
       showLoader();

        $.ajax({
            type: "POST", //GET, POST, PUT
            url: httpsapi + "/configuration/yoco/checkout",  //the url to call
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(uuids),
            success: function (data, text) {
                hideLoader();
                console.log(data);
                if (data.status == 'Success') {
                    location.href = data.data.redirectUrl;
                }
                else {
                    $('#checkoutMsg').html(data.message);
                   // showMessage('danger', 'Error', data.message, 5000);
                }
            },
            error: function (request, status, error) {
                console.log(request, status, error);
                hideLoader();
               // showMessage('danger', 'Error', error, 5000);
                  $('#checkoutMsg').html(error);
            }

        });

    }


       function loadCourtEvents(data) {
        calendar.removeAllEvents();
            for (var ii = 0; ii < data.length; ii++) {
            var status = getStatus(data[ii].court_roll_status);
               var event = {
                             id: ii,
                             title: data[ii].court_roll_captured_records+'/'+data[ii].court_roll_records+' '+data[ii].court_roll_infringement_type,
                             start: data[ii].court_roll_court_date,
                             end: data[ii].court_roll_court_date,
                             allDay: true,
                             classNames: 'event-'+status.badge,

                           };
               calendar.addEvent(event)
            }

       }



    function loadCourtRolls(data) {
     loadCourtEvents(data);
        var str = "";
        for (var ii = 0; ii < data.length; ii++) {
            var status = getStatus(data[ii].court_roll_status);
            str += '<tr><td><a href="#!"  id="t' + ii + '" data-link="infringement_notice_reference" class="clsPrintRoll link-primary">' +
                data[ii].court_roll_id + '</a></td>';
            str += '<td>' + data[ii].court_roll_court_name + '</td>';
            str += '<td>' + getDatee(data[ii].court_roll_court_date) + '</td>';
            str += '<td>' + data[ii].court_roll_infringement_type + '</td>';
            str += '<td>' + data[ii].court_roll_records + '</td>';
            str += '<td>' + data[ii].court_roll_captured_records + '</td>';
            str += '<td>' + status.span + '</td>';
            str += '<td class="text-center"><a href="#!" id="' + ii + '" class="btnCourtResult btn btn-outline-primary btn-small">Court Results</a></td></tr>';

        }
        $('#listRolls').html(str);


        $('.btnCourtResult').click(function () {
            selectedRoll = parseInt(this.id);
            getCourtRollsItems();

        });


        $('.clsPrintRoll').click(function () {
            selectedRoll = parseInt(this.id.substr(1));
            getPrintCourtRoll(rolls[selectedRoll].court_roll_id,"New");
        });
    }


    function getPrintCourtRoll(court_roll_id, type) {

        showLoader();
        fetch(httpsapi + "/management/getPrintCourtRoll?court_roll_id=" + court_roll_id + "&type=" + type, {
            method: 'GET', // or 'POST', etc.
            // other fetch options
        })
            .then(response => {
                hideLoader();
                if (!response.ok) {
                    showMessage('danger', 'Error', response.status, 5000);
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.arrayBuffer();
            })
            .then(arrayBuffer => {
                var blob = new Blob([arrayBuffer], { type: "application/pdf" });
                var link = window.URL.createObjectURL(blob);
                var newWindow = window.open(link, "_blank");
            })
            .catch(error => {
                console.error("Fetch error:", error);
                hideLoader();
                showMessage('danger', 'Error', error, 5000);
            });

    }

    $('#btnPrintRegister').click(function () {
        getPrintCourtRoll(rolls[selectedRoll].court_roll_id, "Updated");
    });




    function getCourtRollsItems() {
        showLoader();
        var details = {
            court_roll_id: rolls[selectedRoll].court_roll_id
        }
        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/management/getCourtRollsItems",  //the url to call
            contentType: "application/json",
            data: jQuery.param(details)
        }).done(function (data) {
            console.log(data);
            roll_items = data;
            $('#courtResultsModal').modal('show');
            selectedItem = 0;
            updateTable(roll_items);

            hideLoader();
        }).fail(function (err) {
            console.log(err.statusText);
            hideLoader();
            showMessage('danger', 'Error', err.statusText, 5000);

        });
    }
    function updateTable(data) {
        var status = getStatus(rolls[selectedRoll].court_roll_status);
        $('.clsRollId').html(rolls[selectedRoll].court_roll_id);
        $('.clsRollStatus').html(status.span);
        var str = "";
        for (var ii = 0; ii < data.length; ii++) {
            var status = getStatus(data[ii].court_roll_item_status);
            var fine = data[ii].court_roll_item_fine_amount;
            str += '<tr id="w' + ii + '"  class="trItem btn-link-primary"><td>' + data[ii].court_roll_item_id + '</td>';
            str += '<td>' + data[ii].court_roll_item_notice_reference + '</td>';
            str += '<td>' + (fine == 'NAG' ? 'NAG' : 'R' + parseFloat(fine).toFixed(2)) + '</td>';
            str += '<td>' + removeNull(data[ii].court_roll_item_results) + '</td>';
            str += '<td>' + status.span + '</td></tr>';
        }
        $('#listRollItems').html(str);

        $('.trItem').click(function () {
            selectedItem = parseInt(this.id.substr(1));
            $('.trItem').removeClass('active');
            $(this).addClass('active');
            loadItem();
        });
        $(document).find('#w' + selectedItem).trigger('click');
    }

    function loadItem() {
        $('.clsCaseNumber').text(roll_items[selectedItem].court_roll_item_id);
        $('.txtFeedback').val('').removeClass('is-valid').removeClass('is-invalid');
        $('.divFeedback').hide();
        $('#cboFeedback').val(removeNull(roll_items[selectedItem].court_roll_item_results)).trigger('change');
        $('#txtNewCourtDate').val(removeNull(roll_items[selectedItem].court_roll_item_results_new_court_date));
        $('#txtPaidAmount').val(removeZero(roll_items[selectedItem].court_roll_item_results_fine_amount));
        $('#txtReceiptNumber').val(removeZero(roll_items[selectedItem].court_roll_item_results_invoice_number));
        $('#txtNewFineAmount').val(removeZero(roll_items[selectedItem].court_roll_item_results_reduced_to_amount));
        $('#txtRemarks').val(removeNull(roll_items[selectedItem].court_roll_item_results_description));


        $('#btnSaveProceed').hide();
        $('#btnSubmitResults').hide();
        $('.txtFeedback').attr('disabled', 'disabled');
        if (rolls[selectedRoll].court_roll_status != 'Submitted') {
            $('.txtFeedback').removeAttr('disabled');
            $('#btnSaveProceed').show();
            var complete = true;
            roll_items.forEach((item, key) => {
                if (item.court_roll_item_status != 'Captured')
                    complete = false;
            });

            if (complete)
                $('#btnSubmitResults').show();

        }
       
    }

    $('#btnSaveProceed').click(function () {
        if (validateFields1('requiredFeedback'))
            return;
        $('.txtFeedback:not(#txtRemarks):not(.requiredFeedback)').val('');
        var roll_item = JSON.parse(JSON.stringify(roll_items[selectedItem]));
        roll_item.court_roll_item_results = $('#cboFeedback').val();
        roll_item.court_roll_item_results_new_court_date = !$('#txtNewCourtDate').val() ? null : $('#txtNewCourtDate').val() + 'T00:00:00';;
        roll_item.court_roll_item_results_fine_amount = !$('#txtPaidAmount').val() ? 0 : $('#txtPaidAmount').val();
        roll_item.court_roll_item_results_invoice_number = !$('#txtReceiptNumber').val() ? 0 : $('#txtReceiptNumber').val();
        roll_item.court_roll_item_results_reduced_to_amount = !$('#txtNewFineAmount').val() ? 0 : $('#txtNewFineAmount').val();
        roll_item.court_roll_item_results_description = $('#txtRemarks').val();
        roll_item.court_roll_item_status = 'Captured';
        roll_item.court_roll_item_results_captured_by_name = person_full_name;
        roll_item.court_roll_item_results_captured_by = person_id;
        

        showLoader();

        console.log(roll_item);
        
        $.ajax({
            type: "PUT", //GET, POST, PUT
            url: httpsapi + "/management/updateCourtRollItem/" + roll_item.court_roll_item_id,  //the url to call
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(roll_item),
            success: function (data, text) {
                hideLoader();
                console.log(data);
                if (data.status == 'Success') {
                    roll_items[selectedItem] = data.data;
                    rolls[selectedRoll].court_roll_status = 'Captured';
                    rolls[selectedRoll].court_roll_captured_records = data.extra;
                    loadCourtRolls(rolls);
                    $('#btnNext').trigger('click');
                }
                else {
                    showMessage('danger', 'Error', data.message, 5000);
                }
            },
            error: function (request, status, error) {
                console.log(request, status, error);
                hideLoader();
                showMessage('danger', 'Error', request.responseText, 5000);
            }

        });


    });





    $('#btnSubmitResults').click(function () {
        $('#submitResultsModal').modal('show');
        $('.txtConfirm').val('');
    });

    $('#btnConfirmSubmit').click(function () {
        if (validateFields1('requiredResults'))
            return;
        
        rolls[selectedRoll].court_roll_court_public_prosecutor = $('#txtPublicProsecutor').val();
        rolls[selectedRoll].court_roll_court_preciding_officer = $('#txtPrecidingOfficer').val();
        rolls[selectedRoll].court_roll_court_clerk = $('#txtCourtClerk').val();
        rolls[selectedRoll].court_roll_status = 'Submitted';
        rolls[selectedRoll].court_roll_results_captured_by_name = person_full_name;
        rolls[selectedRoll].court_roll_results_captured_by = person_id;

        showLoader();

        console.log(rolls[selectedRoll]);

        $.ajax({
            type: "PUT", //GET, POST, PUT
            url: httpsapi + "/management/saveCourtResults/" + rolls[selectedRoll].court_roll_id,  //the url to call
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(rolls[selectedRoll]),
            success: function (data, text) {
                hideLoader();
                console.log(data);
                if (data.status == 'Success') {
                    getCourtRolls();
                    showMessage('success', 'success', data.message, 3000);
                    $('#submitResultsModal').modal('hide');
                    $('#courtResultsModal').modal('hide');
                }
                else {
                    showMessage('danger', 'Error', data.message, 5000);
                }
            },
            error: function (request, status, error) {
                console.log(request, status, error);
                hideLoader();
                showMessage('danger', 'Error', request.responseText, 5000);
            }

        });


    });







    $('#btnNext').click(function () {
        selectedItem = selectedItem < roll_items.length - 1 ? selectedItem + 1 : 0;
        updateTable(roll_items);
    });


    $('#cboFeedback').change(function () {
        $('.divFeedback').hide();
        $('.txtFeedback:not(#cboFeedback)').removeClass('requiredFeedback');

         if ($(this).val() == 'Admission of guilt paid') {
             $('.divPaid').show('fast');
             $('.divPaid').find("input").addClass('requiredFeedback');
        }
        else if ($(this).val() == 'Postponed: Accuse warned') {
             $('.divPostponed').show('fast');
             $('.divPostponed').find("input").addClass('requiredFeedback');
        }
        else if ($(this).val() == 'Reduced by prosecutor') {
             $('.divReduced').show('fast');
             $('.divReduced').find("input").addClass('requiredFeedback');
        }
         else {
             if ($(this).val())
                $('.divRemarks').show('fast');
        }
       
    });



    $('#btnNewRoll').click(function () {
        $('#newRollModal').modal('show');
    });



    $('#cboCourtNameIndex').change(function () {
        selectedCourt = defaultData.courts[parseInt($(this).val())];
    });


    $('#btnPrint').click(function () {
        if (validateFields1('requiredRoll')) 
            return;

        var court_roll = {};
        court_roll.court_roll_infringement_type = $("#cboNoticeType option:selected").text();
        court_roll.court_roll_court_address = selectedCourt.court_physical_address;
        court_roll.court_roll_court_id = selectedCourt.court_id;
        court_roll.court_roll_court_name = selectedCourt.court_office + ' ' + selectedCourt.court_type;
        court_roll.court_roll_created_by = person_id;
        court_roll.court_roll_created_by_name = person_full_name;
        court_roll.court_roll_client_id = client_id;
        court_roll.court_roll_authority_name = client_name;


        console.log(court_roll);
     
        showLoader();

        $.ajax({
            type: "POST", //GET, POST, PUT
            url: httpsapi + "/management/createCourRoll",  //the url to call
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(court_roll),
            success: function (data, text) {
                hideLoader();
                console.log(data);
                if (data.status == 'Success') {
                    getCourtRolls();
                    getPrintCourtRoll(data.data.court_roll_id, "New");
                    showMessage('success', 'success', data.message, 3000);
                    $('#newRollModal').modal('hide');
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


    });




});