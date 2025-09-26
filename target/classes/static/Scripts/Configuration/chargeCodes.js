$(document).ready(function () {
   






    getChargeCodes();

    function getChargeCodes() {
        showLoader();
        var details = {
            client_id: client_id
        }
        $.ajax({
            type: "GET", //GET, POST, PUT
            url: httpsapi + "/configuration/getChargeCodes",  //the url to call
            contentType: "application/json",
            data: jQuery.param(details)
        }).done(function (data) {
            console.log(data);
            Batchs = data;
            var str = "";
            for (var ii = 0; ii < data.length; ii++) {
                str += '<tr><td>' + (ii + 1) + '</td>';
                str += '<td>' + data[ii]['charge_code'] + '</td>';
                str += '<td>' + data[ii]['charge_code_fine_amount'] + '</td>';
                str += '<td>' + substring(data[ii]['charge_code_short_description'],40) + '</td>';
                str += '<td class="text-lowercase">' + substring(data[ii]['charge_code_vehicle_type'], 30) + '</td>';
                str += '<td>' + data[ii]['charge_code_status'] + '</td>';
                str += '<td class="text-center"><a href="#!"><i class="icon feather icon-edit tblIcon text-primary"></i>';
                str += '</a><a href="#!"><i class="feather icon-trash-2 tblIcon text-danger"></i></a></td></tr>';
            }
            $('#listChargeCodes').html(str);

       

            hideLoader();
        }).fail(function (err) {
            console.log(err.statusText);
            hideLoader();
            showMessage('danger', 'Error', err.statusText, 5000);

        });
    }



















    $('#btnUpload').click(function () {
        $('#fileUpload').trigger('click');
    });

    $('#fileUpload').change(function () {
        var fileUpload = $(this)[0];
        //Validate whether File is valid Excel file.
        var regex = /^([a-zA-Z0-9\s_\\.\-:])+(.xls|.xlsx)$/;
        if (regex.test(fileUpload.value.toLowerCase())) {
            if (typeof (FileReader) != "undefined") {
                var reader = new FileReader();

                //For Browsers other than IE.
                if (reader.readAsBinaryString) {
                    reader.onload = function (e) {
                        ProcessExcel(e.target.result);
                    };
                    reader.readAsBinaryString(fileUpload.files[0]);
                } else {
                    //For IE Browser.
                    reader.onload = function (e) {
                        var data = "";
                        var bytes = new Uint8Array(e.target.result);
                        for (var i = 0; i < bytes.byteLength; i++) {
                            data += String.fromCharCode(bytes[i]);
                        }
                        ProcessExcel(data);
                    };
                    reader.readAsArrayBuffer(fileUpload.files[0]);
                }
            } else {
                showMessage('danger', 'Error', "This browser does not support HTML5.", 5000);            }
        } else {
            showMessage('danger', 'Error', "Please upload a valid Excel file.", 5000);
        }
    });

    function ProcessExcel(data) {
        //Read the Excel File data.
        var workbook = XLSX.read(data, {
            type: 'binary'
        });

        //Fetch the name of First Sheet.
        var firstSheet = workbook.SheetNames[0];

        //Read all rows from First Sheet into an JSON array.
        var excelRows = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[firstSheet]);
        console.log(excelRows);
        var dataArr = Array();
        for (var ii = 0; ii < excelRows.length; ii++) {
            var dataa = {};
            dataa.charge_code_description = excelRows[ii]['Charge'];
            dataa.charge_code = parseInt(excelRows[ii]['Charge Code']);
            dataa.charge_code_fine_amount = excelRows[ii]['FINE_AMOUNT'];
            dataa.charge_code_regulation = excelRows[ii]['Section / Regulation'];
            dataa.charge_code_short_description = excelRows[ii]['Short Description'];
            dataa.charge_code_vehicle_type = excelRows[ii]['VEH_TYPE'];
            dataa.charge_code_type = 'General';
            dataa.charge_code_saved_by = person_id;
            dataa.charge_code_timestamp = getNowT();
            dataa.charge_code_client_id = client_id;
            dataa.charge_code_status = 'active';
            dataArr.push(dataa);
        }
        console.log(dataArr);
        showLoader();
        $.ajax({
            type: "POST", //GET, POST, PUT
            url: httpsapi + "/configuration/saveChargeFile",  //the url to call
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(dataArr),
            success: function (data, text) {
                hideLoader();
                console.log(data);
                if (data.status == 'Success') {
                    showMessage('success', 'success', data.message, 3000);
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