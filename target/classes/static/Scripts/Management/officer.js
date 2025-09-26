$(document).ready(function () {
   
    var books = Array();
    var selectedBook;

    var notices = Array();
    var infringer = {};
    var notice = {};
    var charge_code = null;


    var showEdit = myPrivs.indexOf(8) < 0 ? 'hidden' : '';

    getDefaultData();
    getBookCountByOfficerId();
    function getBookCountByOfficerId() {
        var details = {
            book_issued_to: person_id
        }
        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/report/getBookCountByOfficerId",  //the url to call
            contentType: "application/json",
            data: jQuery.param(details)
        }).done(function (data) {
            data = JSON.parse(data);
            console.log(data);
            loadOfficerCount(data);
        }).fail(function (err) {
            console.log(err.statusText);

        });
    }


    function loadOfficerCount(data) {
        $(".txtBooks").html(data.Books.all);
        $(".txtHanded").html(data.Books.handed_in);
        $(".txtNotices").html(data.Items.all);
        $(".txtCaptured").html(data.Items.captured);
        $(".txtCapturedAll").html(data.Captured.all);
        $(".txtClosed").html(data.Captured.closed);

        $(".txtHandedPercent").html(toPercent(data.Books.all, data.Books.handed_in));
        $(".txtCapturedPercent").html(toPercent(data.Items.all, data.Items.captured));
        $(".txtClosedPercent").html(toPercent(data.Captured.all, data.Captured.closed));
    }









    getOfficerBooks();

    function getOfficerBooks() {


        showLoader();
        var details = {
            book_issued_to: person_id
        }
        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/capturing/getOfficerBooks",  //the url to call
            contentType: "application/json",
            data: jQuery.param(details)
        }).done(function (data) {
            console.log(data);
            books = data;
            loadBooks();
            $('#viewMyBooksModal').modal('show');


            hideLoader();
        }).fail(function (err) {
            console.log(err.statusText);
            hideLoader();
            showMessage('danger', 'Error', err.statusText, 5000);

        });
    }

    function loadBooks() {
        var str = "";
        for (var ii = 0; ii < books.length; ii++) {
            var status = getStatus(books[ii].book_status);
            str += '<tr><td>' + (ii + 1) + '</td>';
            str += '<td>' + books[ii]['book_type_code'] + ' (' + books[ii]['book_type_name'] + ')</td>';
            str += '<td>' + books[ii]['book_first_number'] + '</td>';
            str += '<td>' + books[ii]['book_last_number'] + '</td>';
            str += '<td>' + books[ii]['book_pages'] + '</td>';
            str += '<td>' + books[ii]['book_pages_completed'] + '</td>';
            str += '<td>' + status.span + '</td>';
            str += '<td>' + getDatee(books[ii]['book_issue_date']) + '</td>';
            str += '<td>' + getDatee(books[ii]['book_handedin_date']) + '</td>';
            str += '<td class="text-center"><a href="#!" id="' + ii + '" class="viewBook"><i class="feather icon-eye tblIcon text-primary"></i></a></td></tr>';
        }
        $('#listBooks').html(str);

        $('.viewBook').click(function () {
            selectedBook = parseInt(this.id);

            $('.txtBookPages').html(books[selectedBook]['book_pages']);
            $('.txtFirstPage').html(books[selectedBook]['book_first_number']);
            $('.txtLastPage').html(books[selectedBook]['book_last_number']);
            $('.txtCompleted').html(books[selectedBook]['book_pages_completed']);

            getNoticeByBookId(books[selectedBook].book_id);
        
        });
    }





    function getNoticeByBookId(book_id) {
        showLoader();
        var details = {
            infringement_notice_book_id: book_id
        }
        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/capturing/getNoticeByBookId",  //the url to call
            contentType: "application/json",
            data: jQuery.param(details)
        }).done(function (data) {
            console.log(data);
            notices = data;
            $('#noticeListModal').modal('show');
            var str = "";
            for (var ii = 0; ii < data.length; ii++) {
                var status = getStatus(data[ii].notice.infringement_notice_status);
                var btn = status.status == 'Saved' ?
                    '<a href="#!" id="' + ii + '" class="editNotice ' + showEdit+'"><i class="feather icon-edit tblIcon text-primary"></i></a>' :
                    '<a href="#!" id="' + ii + '" class="editNotice"><i class="feather icon-eye tblIcon text-primary"></i></a>';
                str += '<tr><td>' + (ii + 1) + '</td>';
                str += '<td>' + data[ii].notice.infringement_notice_reference + '</td>';
                str += '<td>' + removeNull(data[ii].notice.infringement_notice_registration) + '</td>';
                str += '<td>' + removeNull(data[ii].notice.infringement_notice_name) + '</td>';
                str += '<td>' + removeNull(data[ii].notice.infringement_notice_captured_by_name) + '</td>';
                str += '<td>' + removeZero(data[ii].notice.infringement_notice_charge_code) + '</td>';
                str += '<td>' + status.span + '</td>';
                str += '<td>' + getDatee(data[ii].notice.infringement_notice_timestamp) + '</td>';
                str += '<td class="text-center">' + btn + '</td></tr>';

            }
            $('#listNotice').html(str);


            $('.editNotice').click(function () {
                notice = notices[parseInt(this.id)].notice;
                infringer = notices[parseInt(this.id)].infringer;
                charge_code = notices[parseInt(this.id)].charge_code;
                $('#addNoticeModal').modal('show');
                loadNotice();
            });



            hideLoader();
        }).fail(function (err) {
            console.log(err.statusText);
            hideLoader();
            showMessage('danger', 'Error', err.statusText, 5000);

        });
    }


    function loadNotice() {
        $('#step1').trigger('click');
        $('.requiredNotice').removeAttr('disabled');
        $('.triggers').removeAttr('disabled');
        $('#btnClarity').show();
        $('#btnMessege').hide();


        var status = getStatus(notice.infringement_notice_status);
        $('#cboNoticeType1').val(notice.infringement_notice_type);
        $('#txtNoticeReference1').val(notice.infringement_notice_reference);
        setChargeCode();
        $('#txtOffenceDate').val(getDatee(notice.infringement_notice_offence_date));
        $('#cboCourtName').val(notice.infringement_notice_court_id);
        $('#txtCourtDate').val(getDatee(notice.infringement_notice_court_date));
        $('#txtOffenceLocation').val(notice.infringement_notice_offence_location);
        $('#txtOffenceLocationCode').val(notice.infringement_notice_offence_location_code);
        $('#txtVehicleRegistration').val(notice.infringement_notice_registration);
        $('#cboVehicleMake option:contains("' + notice.infringement_notice_vehicle_make + '")').prop('selected', true);
        $('#cboVehicleMake').trigger('change');
        $('#cboVehicleModel option:contains("' + notice.infringement_notice_vehicle_model + '")').prop('selected', true);
        $('#cboVehicleColour option:contains("' + notice.infringement_notice_vehicle_colour + '")').prop('selected', true);
        $('#cboTitle').val(infringer.infringement_infringer_title);
        $('#txtFirstName').val(infringer.infringement_infringer_name);
        $('#txtLastName').val(infringer.infringement_infringer_surname);
        $('#txtCellphoneNumber').val(infringer.infringement_infringer_cellphone);
        $('#txtEmailAddress').val(infringer.infringement_infringer_email);
        $('#txtPhysicalAddress').val(infringer.infringement_infringer_physical_address);
        $('#txtSuburb').val(infringer.infringement_infringer_physical_suburb);
        $('#txtCity').val(infringer.infringement_infringer_physical_city);
        $('#txtPostalCode').val(infringer.infringement_infringer_physical_code);

        if (status.status == 'Saved') {

            $('.triggers').trigger('focusout');
            console.log(notifications);
            validateFields1('requiredNotice');
            for (var n = 0; n < notifications.length; n++) {
                if (notifications[n].notification_notice_id == notice.infringement_notice_id) {
                    $('#btnMessege').attr('notification-index', n);
                    $('#btnMessege').show();
                }
            }
            choices.enable();
        }
        else {
            $('.requiredNotice').removeClass('is-valid').removeClass('is-invalid').attr('disabled', 'disabled');
            $('.triggers').removeClass('is-valid').removeClass('is-invalid').attr('disabled', 'disabled');
            $('#btnClarity').hide();
            $('.choices__inner').removeClass('is-valid').removeClass('is-invalid');
            choices.disable();
        }

        if (notice.infringement_notice_type == 10) {
            $('#divContact').removeClass('stepwizard-step').hide();
            $('.cls341').hide();
            $('.cls341 .requiredNotice').removeClass('requiredNotice2');
            $('.cls341L .requiredNotice').removeClass('requiredNotice3');
        }
        else {
            $('#divContact').addClass('stepwizard-step').show();
            $('.cls341').show();
            $('.cls341 .requiredNotice').addClass('requiredNotice2');
            $('.cls341L .requiredNotice').addClass('requiredNotice3');
        }
        navListItems = $('div.setup-panel div.stepwizard-step a');

    }


    function setChargeCode() {
        console.log(defaultData);
        var group = Array();
        defaultData.charge = Array();
        for (var i = 0; i < defaultData.charge_codes.length; i++) {
            var index = group.indexOf(defaultData.charge_codes[i].charge_code_vehicle_type.trim());
            if (index < 0) {
                group.push(defaultData.charge_codes[i].charge_code_vehicle_type.trim());
                defaultData.charge.push({
                    label: defaultData.charge_codes[i].charge_code_vehicle_type,
                    choices: [{
                        value: i,
                        label: defaultData.charge_codes[i].charge_code,
                        selected: notice.infringement_notice_charge_code == defaultData.charge_codes[i].charge_code, true: false
                    }]

                });
            }
            else {
                defaultData.charge[index].choices.push(
                    {
                        value: i,
                        label: defaultData.charge_codes[i].charge_code,
                        selected: notice.infringement_notice_charge_code == defaultData.charge_codes[i].charge_code, true: false

                    }
                );
            }

        }
        choices.removeActiveItems();
        choices.setChoices(defaultData.charge);
        console.log(choices.getValue());
        choices.passedElement.element.blur();

        choices.passedElement.element.addEventListener('change', function (event) {
            const selectedValues = choices.getValue(true); // Returns array of selected objects
            if (selectedValues.length == 0) {
                $('.choices__inner').removeClass('is-valid').addClass('is-invalid');
                charge_code = null;
            }
            else {
                charge_code = JSON.parse(JSON.stringify(defaultData.charge_codes[selectedValues[0]]));
                $('.choices__inner').removeClass('is-invalid').addClass('is-valid');
            }

            console.log(charge_code);
        });

        choices.passedElement.element.dispatchEvent(new Event('change'));



    }



   

    $('#btnClarity').click(function () {
        notice.infringement_notice_saved_note = "";
        notice.infringement_notice_saved_by = person_id;
        notice.infringement_notice_saved_by_name = person_full_name;
        notice.infringement_notice_status = "Saved";
        saveNotice();

    });



   




    $('#txtIDNumber').focusout(function () {
        infringer.infringement_infringer_id_number = null;
        if (validateIdNumber($(this)).valid)
            infringer.infringement_infringer_id_number = $(this).val();
    });
    $('#txtIDNumber').focusin(function () {
        $(this).parent('div').find('label').text('ID Number');
        //  $(this).removeClass('is-invalid').removeClass('is-valid');
    });



    $('.requiredNotice').change(function () {
        validateFieldsOne($(this));
    });






    $('#btnSubmitNotice').click(function () {

        if ($(this).text() == 'Submit') {
            $('.triggers').trigger('focusout');
            validateFields1('requiredNotice1'); validateFields1('requiredNotice2'); validateFields1('requiredNotice3');

            if (!notice.infringement_notice_reference || !charge_code || validateFields1('requiredNotice1')) {
                $('#step1').trigger('click');
                return;
            }
            else if ((!infringer.infringement_infringer_id_number && notice.infringement_notice_type != 10)  || validateFields1('requiredNotice2')) {
                $('#step2').trigger('click');
                return;
            }
            else if (validateFields1('requiredNotice3')) {
                $('#step3').trigger('click');
                return;
            }
            else {
                notice.infringement_notice_saved_note = "Notice submitted by " + person_full_name;
                notice.infringement_notice_status = "Notice";
                notice.infringement_notice_submitted_by = person_id;
                notice.infringement_notice_submitted_by_name = person_full_name;
                saveNotice();
            }

        }
    });





    function saveNotice() {


        notice.infringement_notice_charge_code = charge_code ? charge_code.charge_code : 0;
        notice.infringement_notice_fine_amount = charge_code ? charge_code.charge_code_fine_amount : '';
        notice.infringement_notice_charge_short_description = charge_code ? charge_code.charge_code_short_description : '';
        notice.infringement_notice_charge_description = charge_code ? replaceHolders(charge_code.charge_code_description, notice) : '';
        notice.infringement_notice_final_amount = !charge_code || charge_code.charge_code_fine_amount == 'NAG' ? 0 : parseFloat(charge_code.charge_code_fine_amount);

        notice.infringement_notice_offence_date = $('#txtOffenceDate').val() ? $('#txtOffenceDate').val() + 'T00:00:00' : null;

        notice.infringement_notice_court_id = $('#cboCourtName').val() ? parseInt($('#cboCourtName').val()) : 0;
        notice.infringement_notice_court_name = $('#cboCourtName').val() ? $("#cboCourtName option:selected").text() : "";
        notice.infringement_notice_court_date = $('#txtCourtDate').val() ? $('#txtCourtDate').val() + 'T00:00:00' : null;

        notice.infringement_notice_offence_location = $('#txtOffenceLocation').val();
        notice.infringement_notice_offence_location_code = $('#txtOffenceLocationCode').val();
        notice.infringement_notice_registration = $('#txtVehicleRegistration').val();
        notice.infringement_notice_vehicle_make = $('#cboVehicleMake').val() ? $("#cboVehicleMake option:selected").text() : '';
        notice.infringement_notice_vehicle_model = $('#cboVehicleModel').val() ? $("#cboVehicleModel option:selected").text() : '';
        notice.infringement_notice_vehicle_colour = $('#cboVehicleColour').val() ? $("#cboVehicleColour option:selected").text() : '';


        infringer.infringement_infringer_court_date = $('#txtCourtDate').val() ? $('#txtCourtDate').val() + 'T00:00:00' : null;
        infringer.infringement_infringer_title = $('#cboTitle').val();
        infringer.infringement_infringer_name = $('#txtFirstName').val();
        infringer.infringement_infringer_surname = $('#txtLastName').val();
        infringer.infringement_infringer_cellphone = $('#txtCellphoneNumber').val();
        infringer.infringement_infringer_email = $('#txtEmailAddress').val();
        infringer.infringement_infringer_physical_address = $('#txtPhysicalAddress').val();
        infringer.infringement_infringer_physical_suburb = $('#txtSuburb').val();
        infringer.infringement_infringer_physical_city = $('#txtCity').val();
        infringer.infringement_infringer_physical_code = $('#txtPostalCode').val();

        var details = {
            notice: notice,
            infringer: infringer,
            charge_code: charge_code
        };

        console.log(details, httpsapi + "/capturing/saveNotice", JSON.stringify(details));
        showLoader();

        $.ajax({
            type: "POST", //GET, POST, PUT
            url: httpsapi + "/capturing/saveNotice",  //the url to call
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(details),
            success: function (data, text) {
                hideLoader();
                console.log(data);
                if (data.status == 'Success') {
                    getNoticeByBookId(books[selectedBook].book_id);
                    showMessage('success', 'success', data.message, 3000);
                    $('#addNoticeModal').modal('hide');
                   
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
                if (parseInt($item.text()) == navListItems.length) {
                    allNextBtn.text('Submit');
                    if (notice.infringement_notice_status == 'Notice')
                        allNextBtn.hide();

                    if (myPrivs.indexOf(48) < 0)
                        allNextBtn.addClass('hidden');
                }
                else {
                    allNextBtn.text('Next');
                    allNextBtn.show();
                }
            }, 10);




        }
    });

    allNextBtn.click(function () {
        console.log($(this).text());
        if ($(this).text() == 'Next') {
            nextStepWizard = $('div.setup-panel div a.btn-primary').parent().next().children("a");
            //    curInputs = curStep.find("input[type='text'],input[type='url']"),
            //    isValid = true;


            nextStepWizard.trigger('click');
        }
    });

    $('div.setup-panel div a.btn-primary').trigger('click');













    $('.btn-check').change(function () {
        status = $(this).val();
        console.log(status);
        $("#listBooks tr").each(function () {
            if ($(this).text().search(new RegExp(status, "i")) < 0) {
                $(this).fadeOut();
            } else {
                $(this).show();
            }
        });
    });





});