$(document).ready(function () {
   
    var selected;
    var infringements = [];
    var checkedList = [];
    var payType;


    if (myPrivs.indexOf(30) < 0)
        $('.btnPayment').addClass('hidden');
    if (myPrivs.indexOf(31) < 0)
        $('.btnReduce').addClass('hidden');
    if (myPrivs.indexOf(32) < 0)
        $('.btnWithdraw').addClass('hidden');
    if (myPrivs.indexOf(33) < 0)
        $('.btnNominate').addClass('hidden');
    var showPrintOutstanding = myPrivs.indexOf(47) < 0 ? 'hidden' : '';

    if (myPrivs.indexOf(30) < 0 && myPrivs.indexOf(31) < 0 && myPrivs.indexOf(32) < 0 && myPrivs.indexOf(33) < 0)
        $('.divManage').addClass('hidden');

   // getInfringementsAccessStatus();

    function getInfringementsAccessStatus() {



        showLoader();
        var details = {
            client_id: client_id,
            access_status: $('#cboAccessStatus').val(),
            search_with: $('#cboSearchWith').val(),
            search_keyword: $('#txtSearch').val().trim()
        }
        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/management/getInfringementsAccessStatus",  //the url to call
            contentType: "application/json",
            data: jQuery.param(details)
        }).done(function (data) {
            console.log(data);
            infringements = data;
            var str = "";
            for (var ii = 0; ii < data.length; ii++) {
                var status = getStatus(data[ii].notice.infringement_notice_status);
                var fine = data[ii].notice.infringement_notice_final_amount;
                var checkBox = data[ii].notice.infringement_notice_access_status == 'Closed' || fine == 0?'':
                    '<div class="form-check"><input data-index="' + ii + '" class="form-check-input checkNotice" type="checkbox"></div>';
                var canPrint = !data[ii].notice.infringement_notice_id_number ? ' hidden="hidden" ' : '';

                str += '<tr><td>' + checkBox+'</td>';
                str += '<td>' + (ii + 1) + '</td>';
                str += '<td><a href="#!" data-link="infringement_notice_reference" class="linkSearch link-primary">' +
                    data[ii].notice.infringement_notice_reference + '</a></td>';
                str += '<td><a href="#!" data-link="infringement_notice_registration" class="linkSearch link-primary">' +
                    data[ii].notice.infringement_notice_registration + '</a></td>';
                str += '<td><a href="#!" data-link="infringement_notice_id_number" class="linkSearch link-primary">' +
                    removeNull(data[ii].notice.infringement_notice_id_number) + '</a></td>';
                str += '<td>' + data[ii].infringer.infringement_infringer_name + ' ' + data[ii].infringer.infringement_infringer_surname + '</td>';
                str += '<td>' + (fine == 0 ? 'NAG' : 'R' + parseFloat(fine).toFixed(2)) + '</td>';
                str += '<td>' + getDatee(data[ii].notice.infringement_notice_offence_date) + '</td>';
                str += '<td>' + status.span + '</td>';
                str += '<td class="text-center"><a href="#!" id="' + ii + '" class="viewInfringment"><i class="feather icon-eye tblIcon text-primary avtar-xs"></i></a>';
                str += '<a href="#!" ' + canPrint + ' id="' + ii + '" class="printOutstanding ' + showPrintOutstanding+'"><i class="feather icon-printer tblIcon text-primary"></i></a></td></tr>';

            }
            $('#listInfringments').html(str);

            $('.linkSearch').click(function () {
                $('#cboSearchWith').val($(this).attr('data-link'));
                $('#txtSearch').val($(this).text());
                $('#btnSearch').trigger('click');
            });

            $(".checkNotice").change(function () {
                $('.checkNotice:checked').map(function (x) {
                    console.log(x, $(this).attr('data-index'));
                });
                if ($('.checkNotice:checked').length > 0)
                    $('#btnPayList').removeClass('disabled');
                else
                    $('#btnPayList').addClass('disabled');


                if ($('.checkNotice:checked').length == $('.checkNotice').length)
                    $("#checkAllNotice").prop('checked', true);
                else
                    $("#checkAllNotice").prop('checked', false);
            });

            $('.viewInfringment').click(function () {
                $(".checkNotice").prop('checked', false);
                $('input[data-index="' + this.id + '"]').prop('checked', true);
                selected = parseInt(this.id);
                loadInfringement();
                $('#infringmentModal').modal('show');
            });

            $('.printOutstanding').click(function () {
                selected = parseInt(this.id);
                printOutstanding();
            });


            

            hideLoader();
        }).fail(function (err) {
            console.log(err);
            hideLoader();
            showMessage('danger', 'Error', err.statusText, 5000);

        });
    }


    function printOutstanding() {

        showLoader();
        fetch(httpsapi + "/management/printOutstanding?client_id=" + client_id +
            "&infringement_notice_id_number=" + infringements[selected].notice.infringement_notice_id_number, {
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







    $('txtSearch').on('keyup', function (evt) {

        if (evt.which == 13) {
            if (validateFields1('validSearch'))
                return;
            $("#checkAllNotice").prop('checked', false);
            getInfringementsAccessStatus();
        }
        //evt.preventDefault();
    });



    $('#btnSearch').click(function () {
        if (validateFields1('validSearch'))
            return;
        $("#checkAllNotice").prop('checked', false);
        getInfringementsAccessStatus();
    });

    $('#txtSearch').change(function () {
        $(this).removeClass('is-valid').removeClass('is-invalid');
    }).keyup(function () {
        $(this).removeClass('is-valid').removeClass('is-invalid');
    });

    
    $("#checkAllNotice").change(function () {
        $('.checkNotice').prop('checked', $(this).is(':checked'));
        if ($('.checkNotice:checked').length > 0)
            $('#btnPayList').removeClass('disabled');
        else
            $('#btnPayList').addClass('disabled');
    });
    

    function loadInfringement() {
        var status = getStatus(infringements[selected].notice.infringement_notice_status);
        var fine = infringements[selected].notice.infringement_notice_final_amount;

        $('.infringement_notice_status').html(status.status).removeClass('bg-light-primary').removeClass('bg-light-success').removeClass('bg-light-danger').removeClass('bg-light-warning').addClass('bg-light-' + status.badge);
        $('.infringement_notice_reference').html(infringements[selected].notice.infringement_notice_reference);
        $('.infringement_notice_payment_due_date').html(getDatee(infringements[selected].notice.infringement_notice_payment_due_date));
        $('.infringement_notice_court_name').html(infringements[selected].notice.infringement_notice_court_name);
        $('.infringement_notice_final_amount').html(fine == 0 ? 'NAG' : 'R' + parseFloat(fine).toFixed(2));
        $('.infringement_notice_offence_date').html(getDatee(infringements[selected].notice.infringement_notice_offence_date));
        $('.infringement_notice_officer_name').html(infringements[selected].notice.infringement_notice_officer_name);
        $('.infringement_notice_offence_location').html(infringements[selected].notice.infringement_notice_offence_location);
        $('.infringement_notice_registration').html(infringements[selected].notice.infringement_notice_registration);
        $('.infringement_notice_vehicle_make').html(infringements[selected].notice.infringement_notice_vehicle_make);
        $('.infringement_notice_vehicle_model').html(infringements[selected].notice.infringement_notice_vehicle_model);
        $('.infringement_notice_vehicle_colour').html(infringements[selected].notice.infringement_notice_vehicle_colour);

        $('.infringement_infringer_court_date').html(getDatee(infringements[selected].infringer.infringement_infringer_court_date));
        $('.infringement_infringer_id_number').html(removeNull(infringements[selected].infringer.infringement_infringer_id_number));
        $('.infringement_infringer_title').html(infringements[selected].infringer.infringement_infringer_title);
        $('.infringement_infringer_name').html(infringements[selected].infringer.infringement_infringer_name);
        $('.infringement_infringer_surname').html(infringements[selected].infringer.infringement_infringer_surname);
        $('.infringement_infringer_cellphone').html(infringements[selected].infringer.infringement_infringer_cellphone);
        $('.infringement_infringer_email').html(infringements[selected].infringer.infringement_infringer_email);
        $('.infringement_infringer_physical_address').html(infringements[selected].infringer.infringement_infringer_physical_address);
        $('.infringement_infringer_physical_suburb').html(infringements[selected].infringer.infringement_infringer_physical_suburb);
        $('.infringement_infringer_physical_city').html(infringements[selected].infringer.infringement_infringer_physical_city);
        $('.infringement_infringer_physical_code').html(infringements[selected].infringer.infringement_infringer_physical_code);

        $('.charge_code_short_description').html(infringements[selected].charge_code.charge_code_short_description);
        $('.charge_code_description').html(replaceHolders(infringements[selected].charge_code.charge_code_description, infringements[selected].notice));
        $('.charge_code_regulation').html(infringements[selected].charge_code.charge_code_regulation);
        var finee = infringements[selected].charge_code.charge_code_fine_amount;
        $('.charge_code_fine_amount').html(finee == 'NAG' ? finee : 'R' + parseFloat(finee).toFixed(2));
        $('.charge_code_vehicle_type').html(infringements[selected].charge_code.charge_code_vehicle_type);
        $('.charge_code_type').html(infringements[selected].charge_code.charge_code_type);
        $('.charge_code').html(infringements[selected].charge_code.charge_code);

        $('.requiredReduce').val('').removeClass('is-valid').removeClass('is-invalid');
        $('.requiredWithdraw').val('').removeClass('is-valid').removeClass('is-invalid');
        $('#txtCurrentFine').val(fine == 0 ? 'NAG' : fine);


        loadHistory(infringements[selected].historyList);

        $('.divManage').hide();
        $('.clsInvoice').hide();
        if (infringements[selected].notice.infringement_notice_access_status == 'Open') {
            $('.divManage').show();
            $('.linkManage').show();
            if (fine == 0)
                $('.linkPayment').hide();
        }
        else if (infringements[selected].notice.infringement_notice_status == 'Paid') {
            $('.clsInvoice').show();
        }

    }


    function loadHistory(history) {
        
        $('.divHistory').html('');
        for (var i = 0; i < history.length; i++) {
            var data = [];
            if (history[i].history_action == 'Notice Captured' || history[i].history_action == 'Enatis Captured' || history[i].history_action == 'Enatis Imported') {
                var fine = (history[i].history_value == 'NAG' ? 'NAG' : 'R' + parseFloat(history[i].history_value).toFixed(2))
                data.push(['bg-primary', 'save']);
                data.push(['Fine', fine]);
                data.push(['Id Number', removeNull(history[i].history_value5)]);
                data.push(['Driver Name', removeNull(history[i].history_value6)]);
                data.push(['Registration', history[i].history_value7]);
                data.push(['Notice Reference', history[i].history_value8]);
            }
            else if (history[i].history_action == "Court Roll Printed") {
                data.push(['bg-info', 'printer']);
                data.push(['Case Number', history[i].history_value6]);
                data.push(['Court Name', history[i].history_value]);
                data.push(['Court Date', getDatee(history[i].history_value1)]);
                data.push(['Charge Code', history[i].history_value4]);
                data.push(['Notice Reference', history[i].history_value8]);
                
            }
            else if (history[i].history_action == 'Notice Reduced') {
                var fineO = (history[i].history_value2 == 'NAG' || history[i].history_value2 == '0' ? 'NAG' : 'R' + parseFloat(history[i].history_value2).toFixed(2))
                data.push(['bg-warning', 'trending-down']);
                data.push(['Original Fine', fineO]);
                data.push(['Reduced Amount', 'R' + parseFloat(history[i].history_value3).toFixed(2)]);
                data.push(['Notice Reference', history[i].history_value8]);
                data.push(['Reduction Reason', history[i].history_value == 'Other' ? history[i].history_value1: history[i].history_value]);
              
            }
            else if (history[i].history_action == 'Notice Withdrawn') {
                var fineO = (history[i].history_value2 == 'NAG' ? 'NAG' : 'R' + parseFloat(history[i].history_value2).toFixed(2))
                data.push(['bg-success', 'trending-down']);
                data.push(['Notice Reference', history[i].history_value8]);
                data.push(['Withdrawn Reason', history[i].history_value == 'Other' ? history[i].history_value1 : history[i].history_value]);
                data.push(['', '']);
            }
            else if (history[i].history_action == 'Warrant Issued') {
                var fineO = (history[i].history_value3 == '0' ? 'NAG' : 'R' + parseFloat(history[i].history_value3).toFixed(2))
                data.push(['bg-danger', 'trending-down']);
                data.push(['Notice Reference', history[i].history_value8]);
                data.push(['Contempt Of Court', 'R' + parseFloat(history[i].history_value2)]);
                data.push(['New Amount', fineO]);
                data.push(['Reason', history[i].history_value1]);
            }
            else if (history[i].history_action == 'Notice Postponed') {
                data.push(['bg-info', 'trending-down']);
                data.push(['Notice Reference', history[i].history_value8]);
                data.push(['Postpone Reason', history[i].history_value == 'Other' ? history[i].history_value1 : history[i].history_value]);
                data.push(['', '']);
            }
            else if (history[i].history_action == 'Court Results Captured') {
                data.push(['bg-primary', 'trending-down']);
                data.push(['Notice Reference', history[i].history_value8]);
                data.push(['Description', history[i].history_value == 'Other' ? history[i].history_value1 : history[i].history_value]);
                data.push(['', '']);
            }
            else if (history[i].history_action == 'Notice Payment') {
                data.push(['bg-success', 'credit-card']);
                data.push(['Paid Amount', 'R' + parseFloat(history[i].history_value4).toFixed(2)]);
                data.push(['Invoice Number', '<a href="#!" class="link-primary clsInvoice">' + history[i].history_value2 + '</a>']);
                data.push(['Invoice Total', 'R' + parseFloat(history[i].history_value3).toFixed(2)]);
                data.push(['Payment Type', history[i].history_value]);
                data.push(['Payment Date', getDatee(history[i].history_value1)]);

            }
            else if (history[i].history_action == 'Notice Expired') {
                data.push(['bg-success', 'trending-down']);
                data.push(['Notice Reference', history[i].history_value8]);
                data.push(['Withdrawn Reason', history[i].history_value1]);
                data.push(['', '']);
            }

            if (data.length > 0) {
                var str = '<div class="row p-t-20 p-b-30"><div class="col-auto text-end update-meta">' +
                    '<p class="text-muted m-b-0 d-inline-flex">' + getDatee(history[i].history_action_date) + '</p>' +
                    '<i class="feather icon-' + data[0][1] +' ' + data[0][0]+' update-icon"></i></div>' +
                    '<div class="col"><div class="d-flex align-items-center mb-2 justify-content-between">' +
                    '<h6>' + history[i].history_action + '</h6><div>' +
                    '<span class="mb-1 f-12 text-muted">User: </span>' +
                    '<span class="mb-0 text-capitalize">' + history[i].history_action_by_name + '</span></div></div>' +
                    '<div class="card-body pb-0"><ul class="list-group list-group-flush">' +
                    '<li class="list-group-item px-0 pt-0"><div class="row">';
                for (var d = 1; d < data.length; d++) {
                    str += '<div class="col-md"><p class="mb-1 f-12 text-muted">' + data[d][0] + '</p><p>' + data[d][1] +'</p></div>';
                }
                str += '</div></li></ul></div></div></div>';
                $('.divHistory').append(str);
            }
        }

        $('.clsInvoice').click(function () {
            getInvoice(infringements[selected].invoiceData.invoice.invoice_number);
        });
    }




    $('.linkManage').click(function () {
        $('#' + $(this).text() + 'Modal').modal('show');
        $('#infringmentModal').modal('hide');
        payType = $(this).attr('data-pay');

        $('.cancelManage').click(function () {
            if (payType=='one')
            $('#infringmentModal').modal('show');
        });

        if ($(this).text() == 'Payment')
            loadPayment();
        
    });

    
    function loadPayment() {
        checkedList = [];
        $('.checkNotice:checked').map(function (x) {
            checkedList.push(infringements[parseInt($(this).attr('data-index'))]);
        });
        var str = "", total = 0;;
        for (var ii = 0; ii < checkedList.length; ii++) {
            str += '<tr><td><div class="form-check"><input checked data-index="' + ii + '" class="form-check-input checkPay" type="checkbox"></div></td>';
            str += '<td>' + (ii + 1) + '</td>';
            str += '<td>' + checkedList[ii].notice.infringement_notice_reference + '</td>';
            str += '<td>' + checkedList[ii].notice.infringement_notice_registration + '</td>';
            str += '<td>' + ('R' + parseFloat(checkedList[ii].notice.infringement_notice_final_amount).toFixed(2)) + '</td></tr>';
            total += checkedList[ii].notice.infringement_notice_final_amount;
        }
        $('#listPayment').html(str);
        $('#txtTotal').html(parseFloat(total).toFixed(2));

        $(".checkPay").change(function () {
            total = 0;
            $('.checkPay:checked').map(function (x) {
                total += checkedList[parseInt($(this).attr('data-index'))].notice.infringement_notice_final_amount;
                console.log(x, $(this).attr('data-index'));
            });
            $('#txtTotal').html(parseFloat(total).toFixed(2));
        });
    }





    $("#txtReduceReason").change(function () {
        if ($(this).val() == 'Other') {
            $(".divReduceDescription").show('fast');
            $("#txtReduceDescription").addClass('requiredReduce');
        }
        else {
            $(".divReduceDescription").hide('fast');
            $("#txtReduceDescription").removeClass('requiredReduce');
        }
        
    });

    $('#btnReduce').click(function () {
        if (validateFields1('requiredReduce'))
            return;
        showLoader();

        var notice = infringements[selected].notice;
        notice.infringement_notice_status_updated_by_name = person_full_name;
        notice.infringement_notice_status_updated_by = person_id;
        notice.infringement_notice_final_amount = $('#txtNewFine').val();
        notice.infringement_notice_holder_value = $('#txtReduceReason').val();
        notice.infringement_notice_holder_value1 = $('#txtReduceDescription').val();

        console.log(notice);


        $.ajax({
            type: "PUT", //GET, POST, PUT
            url: httpsapi + "/management/reduceNotice/" + notice.infringement_notice_id,  //the url to call
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(notice),
            success: function (data, text) {
                hideLoader();
                console.log(data);
                if (data.status == 'Success') {
                    getInfringementsAccessStatus();
                    showMessage('success', 'success', data.message, 3000);
                    $('#ReduceModal').modal('hide');                }
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





    $("#txtWithdrawReason").change(function () {
        if ($(this).val() == 'Other') {
            $(".divWithdrawDescription").show('fast');
            $("#txtWithdrawDescription").addClass('requiredWithdraw');
        }
        else {
            $(".divWithdrawDescription").hide('fast');
            $("#txtWithdrawDescription").removeClass('requiredWithdraw');
        }

    });

    $('#btnWithdraw').click(function () {
        if (validateFields1('requiredWithdraw'))
            return;
        showLoader();

        var notice = infringements[selected].notice;
        notice.infringement_notice_status_updated_by_name = person_full_name;
        notice.infringement_notice_status_updated_by = person_id;
        notice.infringement_notice_holder_value = $('#txtWithdrawReason').val();
        notice.infringement_notice_holder_value1 = $('#txtWithdrawDescription').val();

        console.log(notice);


        $.ajax({
            type: "PUT", //GET, POST, PUT
            url: httpsapi + "/management/withdrawNotice/" + notice.infringement_notice_id,  //the url to call
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(notice),
            success: function (data, text) {
                hideLoader();
                console.log(data);
                if (data.status == 'Success') {
                    getInfringementsAccessStatus();
                    showMessage('success', 'success', data.message, 3000);
                    $('#WithdrawModal').modal('hide');
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




    $('#btnPayment').click(function () {
        if (validateFields1('requiredPayment'))
            return;
        showLoader();

        checkedListPayment = [];
        $('.checkPay:checked').map(function (x) {
            var notice = checkedList[parseInt($(this).attr('data-index'))].notice;
            notice.infringement_notice_status_updated_by_name = person_full_name;
            notice.infringement_notice_status_updated_by = person_id;
            notice.infringement_notice_holder_value = $('#txtPaymentType').val();
            notice.infringement_notice_holder_value1 = $('#txtPaymentDate').val();
            notice.infringement_notice_holder_value_double = $('#txtTotal').text();
            notice.infringement_notice_holder_value2 = client_name;
            notice.infringement_notice_holder_value3 = notice.infringement_notice_name;
            checkedListPayment.push(notice);
        });

       
        console.log(checkedListPayment);


        $.ajax({
            type: "POST", //GET, POST, PUT
            url: httpsapi + "/management/payNotice",  //the url to call
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(checkedListPayment),
            success: function (data, text) {
                hideLoader();
                console.log(data);
                if (data.status == 'Success') {
                    getInfringementsAccessStatus();
                    getInvoice(data.data.invoice_number);
                    showMessage('success', 'success', data.message, 3000);
                    $('#PaymentModal').modal('hide');
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



    function getInvoice(invoice_number) {
        
        showLoader();
        fetch(httpsapi + "/management/getInvoice?invoice_number=" + invoice_number, {
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





 




});