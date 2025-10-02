$(document).ready(function () {
   
    var notices = Array();
    var notice = {};
    var infringer = {};
    var charge_code = {};
    var tab = "Ready";


    if (myPrivs.indexOf(28) < 0)
        $('#btnImport').addClass('hidden');
    if (myPrivs.indexOf(27) < 0)
        $('#btnExport').addClass('hidden');
    var showCapture = myPrivs.indexOf(50) < 0 ? 'hidden' : '';
    if (showCapture)
        $('.clsAction').hide();

    getEnatis();

    function getEnatis() {
        showLoader();
        var details = {
            client_id: client_id
        }
        console.log(details);
        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/capturing/getEnatis",  //the url to call
            contentType: "application/json",
            data: jQuery.param(details)
        }).done(function (data) {
            console.log(data);
            notices = data;
            loadEnatis();

            hideLoader();
        }).fail(function (err) {
            console.log(err.statusText);
            hideLoader();
            showMessage('danger', 'Error', err.statusText, 5000);

        });
    }




    function loadEnatis() {
        var Ready = 0, Exported = 0, NotFound = 0;
        var str = "";
        for (var ii = 0; ii < notices.length; ii++) {
            var status = getStatus(notices[ii].notice.infringement_notice_enatis_status);
            var btn = '<a href="#!" id="' + ii + '" class="editNotice ' + showCapture +'"><i class="feather icon-edit tblIcon text-primary"  data-bs-toggle="tooltip" data-bs-title="Capture enatis details"></i></a>' ;
            Ready = status.status == 'Ready' ? Ready + 1 : Ready;
            Exported = status.status == 'Exported' ? Exported + 1 : Exported;
            NotFound = status.status == 'Not Found' ? NotFound + 1 : NotFound;
            if (tab == status.status) {
                str += '<tr><td>' + (ii + 1) + '</td>';
                str += '<td>' + notices[ii].notice.infringement_notice_reference + '</td>';
                str += '<td>' + removeNull(notices[ii].notice.infringement_notice_registration) + '</td>';
                str += '<td>' + notices[ii].notice.infringement_notice_type_name + '</td>';
                str += '<td>' + removeNull(notices[ii].notice.infringement_notice_officer_name) + '</td>';
                str += '<td>' + removeZero(notices[ii].notice.infringement_notice_charge_code) + '</td>';
                str += '<td>' + status.span + '</td>';
                str += '<td>' + getDatee(notices[ii].notice.infringement_notice_offence_date) + '</td>';
                str += '<td class="text-center">' + btn + '</td></tr>';
            }
        }
        $('#listEnatis').html(str);

        $("[data-status='Ready']").find('h4').html(Ready);
        $("[data-status='Exported']").find('h4').html(Exported);
        $("[data-status='Not Found']").find('h4').html(NotFound);

        $('.btnImportExport').show();
        if ((Ready + NotFound)==0)
            $("#btnExport").hide();
        if (Exported == 0)
            $("#btnImport").hide();

        $('.editNotice').click(function () {
            notice = notices[parseInt(this.id)].notice;
            infringer = notices[parseInt(this.id)].infringer;
             charge_code = notices[parseInt(this.id)].charge_code;
            $('#addNoticeModal').modal('show');
            loadNotice();
        });

    }


    function loadNotice() {
        $('#step2').trigger('click');
        $('#btnMessege').hide();


        $('#txtIDNumber').val(notice.infringement_notice_id_number);
        $('#cboNoticeType1').val(notice.infringement_notice_type);
        $('#txtNoticeReference1').val(notice.infringement_notice_reference);
        $('#cboChargeCode').val(removeZero(notice.infringement_notice_charge_code));
        $('#txtOffenceDate').val(getDatee(notice.infringement_notice_offence_date));
        $('#cboCourtName').val(notice.infringement_notice_client_name);
        $('#txtCourtDate').val(getDatee(notice.infringement_notice_court_date));
        $('#txtOffenceLocation').val(notice.infringement_notice_offence_location);
        $('#txtOffenceLocationCode').val(notice.infringement_notice_offence_location_code);
        $('#txtVehicleRegistration').val(notice.infringement_notice_registration);

//        $('#cboVehicleMake option:contains("' + notice.infringement_notice_vehicle_make + '")').prop('selected', true);
//        $('#cboVehicleMake').trigger('change');
//        $('#cboVehicleModel option:contains("' + notice.infringement_notice_vehicle_model + '")').prop('selected', true);
//        $('#cboVehicleColour option:contains("' + notice.infringement_notice_vehicle_colour + '")').prop('selected', true);

        $('#cboVehicleMake').val(notice.infringement_notice_vehicle_make);
        $('#cboVehicleModel').val(notice.infringement_notice_vehicle_model);
        $('#cboVehicleColour').val(notice.infringement_notice_vehicle_colour);

        $('#cboTitle').val(infringer.infringement_infringer_title);
        $('#txtFirstName').val(infringer.infringement_infringer_name);
        $('#txtLastName').val(infringer.infringement_infringer_surname);
        $('#txtCellphoneNumber').val(infringer.infringement_infringer_cellphone);
        $('#txtEmailAddress').val(infringer.infringement_infringer_email);
        $('#txtPhysicalAddress').val(infringer.infringement_infringer_physical_address);
        $('#txtSuburb').val(infringer.infringement_infringer_physical_suburb);
        $('#txtCity').val(infringer.infringement_infringer_physical_city);
        $('#txtPostalCode').val(infringer.infringement_infringer_physical_code);

        $('.triggers').trigger('focusout');
        validateFields1('requiredNotice');

       

    }



    $('#btnClarity').click(function () {
        notice.infringement_notice_saved_note = "";
        saveNotice();
    });



//    $('#cboChargeCode').focusout(function () {
//        if ($(this).val().length < 5) {
//            $(this).parent('div').find('label').text('Invalid Charge Code');
//            $(this).addClass('is-invalid');
//            return;
//        }
//        getChargeCode($(this));
//    });
//    $('#cboChargeCode').focusin(function () {
//        $(this).parent('div').find('label').text('Charge Code');
//        // $(this).removeClass('is-invalid').removeClass('is-valid');
//    });

   // getDefaultData();


//    function getChargeCode(elem) {
//        charge_code = null;
//        var code = parseInt(elem.val());
//        elem.addClass('is-invalid');
//        elem.parent('div').find('label').html('Charge Code Not Found');
//        for (var i = 0; i < defaultData.charge_codes.length; i++) {
//            if (defaultData.charge_codes[i].charge_code == code) {
//                charge_code = defaultData.charge_codes[i];
//                elem.removeClass('is-invalid').addClass('is-valid');
//                elem.parent('div').find('label').html(defaultData.charge_codes[i].charge_code_short_description);
//                i = defaultData.charge_codes.length;
//            }
//        }
//    }




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
            else if ((!infringer.infringement_infringer_id_number && notice.infringement_notice_type != 10) || validateFields1('requiredNotice2')) {
                $('#step2').trigger('click');
                return;
            }
            else if (validateFields1('requiredNotice3')) {
                $('#step3').trigger('click');
                return;
            }
            else {
                notice.infringement_notice_saved_note = "Enatis Captured"
                notice.infringement_notice_enatis = "Done";
                notice.infringement_notice_enatis_status = "Captured";
                notice.infringement_notice_enatis_verified_by = person_id;
                notice.infringement_notice_enatis_verified_by_name = person_full_name;
                notice.infringement_notice_enatis_verify_date = getNowT();
                saveNotice();
             
            }

        }
    });





    function saveNotice() {

        notice.infringement_notice_vehicle_make = $('#cboVehicleMake').val();
        notice.infringement_notice_vehicle_model = $('#cboVehicleModel').val();
        notice.infringement_notice_vehicle_colour = $('#cboVehicleColour').val();

        infringer.infringement_infringer_title = $('#cboTitle').val();
        infringer.infringement_infringer_name = $('#txtFirstName').val();
        infringer.infringement_infringer_surname = $('#txtLastName').val();
        infringer.infringement_infringer_cellphone = $('#txtCellphoneNumber').val();
        infringer.infringement_infringer_email = $('#txtEmailAddress').val();
        infringer.infringement_infringer_physical_address = $('#txtPhysicalAddress').val();
        infringer.infringement_infringer_physical_suburb = $('#txtSuburb').val();
        infringer.infringement_infringer_physical_city = $('#txtCity').val();
        infringer.infringement_infringer_physical_code = $('#txtPostalCode').val();

        notice.infringement_notice_cellphone = $('#txtCellphoneNumber').val();
        notice.infringement_notice_email = $('#txtEmailAddress').val();

        notice.infringement_notice_enatis_capture_date = getNowDate();
        notice.infringement_notice_enatis_capture_timestamp = getNowT();

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
                    getEnatis();
                    if(notice.infringement_notice_enatis_status == "Captured")
                        runAutomations(notice.infringement_notice_reference);
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


  function runAutomations(reference) {

        var details = {
            infringement_notice_reference: reference
        }
        console.log(details);
        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/configuration/runAutomations",  //the url to call
            contentType: "application/json",
            data: jQuery.param(details)
        }).done(function (data) {
            console.log(data);
        }).fail(function (err) {
            console.log(err.statusText);
        });
    }











    $(".cardItem").click(function () {
        tab = $(this).attr('data-status');
        loadEnatis();
    });

    $("#btnExport").click(function () {
        exportEnatis();
    });




    function exportEnatis() {

        var url = "/capturing/exportEnatis?client_id=" + client_id +
            "&exported_by=" + person_id + "&exported_by_name=" + person_full_name;
        var fileName;

        showLoader();
        fetch(httpsapi + url,
            {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            }

        )
            .then(response => {
                hideLoader();
                if (!response.ok) {
                    showMessage('danger', 'Error', response.status, 5000);
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
  
                fileName = response.headers.get('Pragma');
                console.log(fileName);
                return response.arrayBuffer();
            })
            .then(arrayBuffer => {
                console.log(arrayBuffer);
                var link = document.createElement('a');
                var blob = new Blob([arrayBuffer], { type: "text/plain" });
                link.href = window.URL.createObjectURL(blob);
                link.download = fileName;
                    document.body.appendChild(link);
                    link.click();
                document.body.removeChild(link);

                getEnatis();
                $("[data-status='Exported']").click();
 
            })
            .catch(error => {
                console.error("Fetch error:", error);
                hideLoader();
                showMessage('danger', 'Error', error, 5000);
            });

    } 


    $("#btnImport").click(function () {
        $("#fileInput").click();
    });


    $('#fileInput').on('change', function (event) {
        var file = event.target.files[0];
        if (file) {
            var reader = new FileReader();
            reader.onload = function (e) {
                var content = e.target.result;
                var lines = content.split("\n");
                $.each(lines, function (index, line) {
                    if (line)
                        console.log(index+"  "+line);
                    else
                        lines.splice(index, 1);
                    // Perform operations on each line here
                });
                console.log(lines);
                importEnatis(lines);
            };
            reader.readAsText(file); // Read the file as text
        }
    });


    function importEnatis(lines) {

        var url = "/capturing/importEnatis?client_id=" + client_id +
            "&imported_by=" + person_id + "&imported_by_name=" + person_full_name;

        showLoader();

        $.ajax({
            type: "POST", //GET, POST, PUT
            url: httpsapi + url,  //the url to call
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(lines),
            success: function (data, text) {
                hideLoader();
                console.log(data);
                if (data.status == 'Success') {
                    showMessage('success', 'success', data.message, 3000);
                    getEnatis();

                     for (var i=0;i<data.data.length;i++) {
                         if(data.data[i].infringement_notice_enatis_status == "Imported" && data.data[i].infringement_notice_cellphone){

                            setTimeout(function(reference) {
                             runAutomations(reference);
                            }, 500, data.data[i].infringement_notice_reference);

                         }
                     }
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

                    if (showCapture)
                        allNextBtn.hide();
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






});