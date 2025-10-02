function showMessage(type,title, msg, duration) {
    $('#messageModal').modal('show');
    $('#messageModalBody').html(msg);
    $('#messageModalTitle').removeClass('text-danger').removeClass('text-success');
    $('#messageModalTitle').removeClass('text-warning').addClass('text-' + type);
    $('#messageModalTitle').html(title);
    if (duration) {
        setTimeout(function () {
            $('#messageModal').modal('hide');
        }, duration);
    }
}


function showConfirm(type, title, msg) {
    $('#confirmModal').modal('show');
    $('#confirmModalBody').html(msg);
    $('#confirmModalTitle').removeClass('text-danger').removeClass('text-success');
    $('#confirmModalTitle').removeClass('text-warning').addClass('text-' + type);
    $('#confirmModalTitle').html(title);
}


function showLoader() {
    $('#loaderModal').modal('show');
}

function hideLoader() {
    $('#loaderModal').modal('hide');
    setTimeout(function () {
        $('#loaderModal').modal('hide');
        setTimeout(function () {
            $('#loaderModal').modal('hide');
        }, 700);
    }, 300);
}






var defaultData;
function getDefaultData() {

    var details = {
        client_id: client_id
    }
    $.ajax({
        type: "GET", //GET, POST, PUT
        url: httpsapi + "/capturing/getDefaultData",  //the url to call
        contentType: "application/json",
        data: jQuery.param(details)
    }).done(function (data) {
        console.log('defaultData',data);
        defaultData = data;
        loadDefaultData(data);

    }).fail(function (err) {
        console.log(err.statusText);
        hideLoader();
        showMessage('danger', 'Error', err.statusText, 5000);

    });
}


function loadDefaultData(data) {
    var str = '<option disabled="disabled" selected value="">Select</option>';
    for (var i = 0; i < data.courts.length; i++) {
        str += '<option value="' + data.courts[i].court_id + '">' + data.courts[i].court_office + ' ' + data.courts[i].court_type + '</option>';
    }
    $('#cboCourtName').html(str);
    str = '<option disabled="disabled" selected value="">Select</option>';
    for (var i = 0; i < data.courts.length; i++) {
        str += '<option value="' + i + '">' + data.courts[i].court_office + ' ' + data.courts[i].court_type + '</option>';
    }
    $('#cboCourtNameIndex').html(str);

    str = '<option disabled="disabled" selected value="">Select</option>';
    for (var i = 0; i < data.makes.length; i++) {
        str += '<option value="' + data.makes[i].vehicle_make_id + '">' + data.makes[i].vehicle_make + '</option>';
    }
    $('#cboVehicleMake').html(str);

    str = '<option disabled="disabled" selected value="">Select</option>';
    for (var i = 0; i < data.colours.length; i++) {
        str += '<option value="' + data.colours[i].vehicle_colour_id + '">' + data.colours[i].vehicle_colour_description + '</option>';
    }
    $('#cboVehicleColour').html(str);

    $('#cboVehicleMake').change(function () {
        loadModelData(data, $(this).val());
    });

  
    

}






function loadModelData(data, vehicle_make_id) {
    var str = '<option disabled="disabled" selected value="">Select</option>';
    for (var i = 0; i < data.models.length; i++) {
        if (data.models[i].vehicle_model_make_id == vehicle_make_id)
            str += '<option value="' + data.models[i].vehicle_model_id + '">' + data.models[i].vehicle_model_description + '</option>';
    }
    $('#cboVehicleModel').html(str);
}













function vaidateFields(clas) {
    $('.' + clas).css('border-color', 'silver');
    var input;
    $('.' + clas).map(function () {
        if (!$(this).val() || $(this).css('border-color') == 'red') {
            $(this).css('border-color', 'red');
            $(this).focus();
            input = $(this);
        }
    }).get();

    if (input) {
        input.focus();
        return true;
    }
    else
        return false;
}

function vaidateFieldsContain(clas,key) {
    $('.' + clas).css('border-color', 'silver');
    var input;
    $('.' + clas).map(function () {
        if (!$(this).val() || $(this).css('border-color') == 'red' || $(this).val().indexOf(key) >= 0) {
            $(this).css('border-color', 'red');
            $(this).focus();
            input = $(this);
        }
    }).get();

    if (input) {
        input.focus();
        return true;
    }
    else
        return false;
}


function vaidateFieldsContain1(clas, key) {
    $('.' + clas).removeClass('is-invalid').removeClass('is-valid');
    var input;
    $('.' + clas).map(function () {
        if (!$(this).val() || $(this).hasClass('is-invalid') || $(this).val().indexOf(key) >= 0) {
            $(this).addClass('is-invalid');
            // $(this).focus();
            input = $(this);
        }
    }).get();

    if (input) {
        // input.focus();
        return true;
    }
    else {
        $(this).addClass('is-valid');
        return false;
    }
        
}


function validateFields1(clas) {
    $('.' + clas).removeClass('is-invalid').removeClass('is-valid');
    var input;
    $('.' + clas).map(function () {
        if (!$(this).val() || $(this).hasClass('is-invalid')) {
            $(this).addClass('is-invalid');
            // $(this).focus();
            input = $(this);
        }
        else
            $(this).addClass('is-valid');
    }).get();

    if (input) {
        // input.focus();
        return true;
    }
    else {
        
        return false;
    }

}


function validateFieldsMinNumber(clas,num) {
    $(clas).removeClass('is-invalid').removeClass('is-valid');
    var input;
    $(clas).map(function () {
        if (!$(this).val() || parseInt($(this).val()) < num) {
            $(this).addClass('is-invalid');
            // $(this).focus();
            input = $(this);
        }
        else
            $(this).addClass('is-valid');
    }).get();

    if (input) {
        // input.focus();
        return true;
    }
    else {

        return false;
    }

}

function validateFieldsLeg(clas,len) {
    $('.' + clas).removeClass('is-invalid').removeClass('is-valid');
    var input;
    $('.' + clas).map(function () {
        if (!$(this).val() || $(this).hasClass('is-invalid') || $(this).val().length<len) {
            $(this).addClass('is-invalid');
            // $(this).focus();
            input = $(this);
        }
        else
            $(this).addClass('is-valid');
    }).get();

    if (input) {
        // input.focus();
        return true;
    }
    else {

        return false;
    }

}


function validateFieldsOne(clas) {
    clas.removeClass('is-invalid').removeClass('is-valid');
    var input;
    
    if (!clas.val() || clas.hasClass('is-invalid')) {
        clas.addClass('is-invalid');
            // $(this).focus();
        input = clas;
    }
        else
        clas.addClass('is-valid');
  

    if (input) {
        // input.focus();
        return true;
    }
    else {

        return false;
    }

}


function validateEmailAddress(thiss)
{
    var email = thiss.val();
    thiss.removeClass('is-invalid').removeClass('is-valid');
    const regex =
        /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    if (!regex.test(email)) {
        thiss.parent('div').find('label').text('Invalid Email Address');
        thiss.addClass('is-invalid');
        return false;
    }
    else {
        thiss.parent('div').find('label').text('Email Address Validated');
        thiss.addClass('is-valid');
        return true;
    }

}

function validatePhone(thiss) {
    var email = thiss.val();
    thiss.removeClass('is-invalid').removeClass('is-valid');
    const regex = /^(\+\d{1,2}\s)?\(?\d{3}\)?[\s.-]?\d{3}[\s.-]?\d{4}$/;
    if (!regex.test(email)) {
        thiss.parent('div').find('label').text('Invalid Phone Number');
        thiss.addClass('is-invalid');
        return false;
    }
    else {
        thiss.parent('div').find('label').text('Phone Number Validated');
        thiss.addClass('is-valid');
        return true;
    }

}




function validatePhoneEmail(clas, type) {
    var regex = type == 'Email Address' ? /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/ : /^(\+\d{1,2}\s)?\(?\d{3}\)?[\s.-]?\d{3}[\s.-]?\d{4}$/;
    $(clas).removeClass('is-invalid').removeClass('is-valid');
    if (!$(clas).val() || $(clas).hasClass('is-invalid') || !regex.test($(clas).val())) {
        $(clas).addClass('is-invalid');
        $(clas).parent('div').find('label').text('Invalid ' + type);
        return false;
    }
    else {
        $(clas).addClass('is-valid');
        $(clas).parent('div').find('label').text(type + ' Validated');
        return true;
    }


}

function validatePhoneEmailOrEmpty(clas, type) {
    var regex = type == 'Email Address' ? /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/ : /^(\+\d{1,2}\s)?\(?\d{3}\)?[\s.-]?\d{3}[\s.-]?\d{4}$/;
    $(clas).removeClass('is-invalid').removeClass('is-valid');
    if (!regex.test($(clas).val()) && $(clas).val()) {
        $(clas).addClass('is-invalid');
        $(clas).parent('div').find('label').text('Invalid ' + type);
        return false;
    }
    else {
        $(clas).addClass('is-valid');
        $(clas).parent('div').find('label').text(type + ' Validated');
        if (!$(clas).val()) {
            $(clas).removeClass('is-valid');
            $(clas).parent('div').find('label').text(type);
        }
        return true;
    }


}






function validateIdNumber(thiss) {

    var idNumber = thiss.val();

    // assume everything is correct and if it later turns out not to be, just set this to false
    var correct = '';

    // SA ID Number have to be 13 digits, so check the length
    if (idNumber.length != 13 || !isNumber(idNumber)) {
        correct = 'ID number does not appear to be authentic';
    }

    // get first 6 digits as a valid date
    var tempDate = new Date(idNumber.substring(0, 2), idNumber.substring(2, 4) - 1, idNumber.substring(4, 6));

    var id_date = tempDate.getDate();
    var id_month = tempDate.getMonth();
    var id_year = tempDate.getFullYear();

    var month = id_month + 1;
    if (month < 10)
        month = '0' + month;
    var day = id_date;
    if (day < 10)
        day = '0' + day;
    var fullDates = id_year + "-" + month + "-" + day;

    

    if (!((tempDate.getYear() == idNumber.substring(0, 2)) && (id_month == idNumber.substring(2, 4) - 1) && (id_date == idNumber.substring(4, 6))) && correct == '') {
        correct = 'ID number does not appear to be authentic';
    }

    var res = {};
    res.dob = id_year + "-" + (id_month + 1) + "-" + id_date;
    var genderCode = idNumber.substring(6, 10);
    res.gender = parseInt(genderCode) < 5000 ? "female" : "male";

    // get country ID for citzenship
    res.citzenship = parseInt(idNumber.substring(10, 11)) == 0 ? "yes" : "no";

    // apply Luhn formula for check-digits
    var tempTotal = 0;
    var checkSum = 0;
    var multiplier = 1;
    for (var i = 0; i < 13; ++i) {
        tempTotal = parseInt(idNumber.charAt(i)) * multiplier;
        if (tempTotal > 9) {
            tempTotal = parseInt(tempTotal.toString().charAt(0)) + parseInt(tempTotal.toString().charAt(1));
        }
        checkSum = checkSum + tempTotal;
        multiplier = (multiplier % 2 == 0) ? 1 : 2;
    }
    if ((checkSum % 10) != 0 && correct == '') {
        correct = 'ID number does not appear to be authentic';
    };


    var age = calculateAge(tempDate);
    res.age = age;
    thiss.removeClass('is-invalid').removeClass('is-valid');
    // if no error found, hide the error message
    if (correct == '') {
        thiss.addClass('is-valid');
        res.message = "ID Number Validated";
        res.valid = true;
    }
    // otherwise, show the error
    else {
        thiss.addClass('is-invalid');
        res.message = correct;
        res.valid = false;
    }
    thiss.parent('div').find('label').text(res.message);
    return res;
}


function calculateAge(birthday) { // birthday is a date
    var ageDifMs = Date.now() - birthday.getTime();
    var ageDate = new Date(ageDifMs); // miliseconds from epoch
    return (ageDate.getUTCFullYear() - 1970);
}

function isNumber(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}

function validateEmail(inputText) {
    var d = false;
    if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(inputText)) {
        d = true;
    }

    if (d == false) {
        var msg = "Please enter valid email address.";
        checkEmptyFields(msg);
    }
    return d;
}

function getDatee(date) {
    if (date)
        return date.substring(0, 10);
    else
        return '';
}

function getTimee(time) {
    if (time && time.length>4)
        return time.substring(0, 5);
    else
        return '';
}

function getFromDate(date) {

    var dd = date.getDate();
    var mm = date.getMonth() + 1; //January is 0!
    var yyyy = date.getFullYear();

    var hr = date.getHours();
    var min = date.getMinutes() + 1; //January is 0!
    var sec = date.getSeconds();

    if (dd < 10) {
        dd = '0' + dd
    }
    if (mm < 10) {
        mm = '0' + mm
    }
    if (hr < 10) {
        hr = '0' + hr
    }
    if (min < 10) {
        min = '0' + min
    }
    if (sec < 10) {
        sec = '0' + sec
    }

   // var currentday = yyyy + "-" + mm + "-" + dd + ' ' + hr + ":" + min + ":" + sec;
    var currentday = yyyy + "-" + mm + "-" + dd;

    return currentday;
}

function getNow() {

    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth() + 1; //January is 0!
    var yyyy = today.getFullYear();

    var hr = today.getHours();
    var min = today.getMinutes() + 1; //January is 0!
    var sec = today.getSeconds();

    if (dd < 10) {
        dd = '0' + dd
    }
    if (mm < 10) {
        mm = '0' + mm
    }
    if (hr < 10) {
        hr = '0' + hr
    }
    if (min < 10) {
        min = '0' + min
    }
    if (sec < 10) {
        sec = '0' + sec
    }

    var currentday = yyyy + "-" + mm + "-" + dd + ' ' + hr + ":" + min + ":" + sec;
    return currentday;
}

function getNowT() {

    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth() + 1; //January is 0!
    var yyyy = today.getFullYear();

    var hr = today.getHours();
    var min = today.getMinutes() + 1; //January is 0!
    var sec = today.getSeconds();

    if (dd < 10) {
        dd = '0' + dd
    }
    if (mm < 10) {
        mm = '0' + mm
    }
    if (hr < 10) {
        hr = '0' + hr
    }
    if (min < 10) {
        min = '0' + min
    }
    if (sec < 10) {
        sec = '0' + sec
    }

    var currentday = yyyy + "-" + mm + "-" + dd + 'T' + hr + ":" + min + ":" + sec;
    return currentday;
}

function getNowDate() {

    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth() + 1; //January is 0!
    var yyyy = today.getFullYear();

    var hr = today.getHours();
    var min = today.getMinutes() + 1; //January is 0!
    var sec = today.getSeconds();

    if (dd < 10) {
        dd = '0' + dd
    }
    if (mm < 10) {
        mm = '0' + mm
    }
    if (hr < 10) {
        hr = '0' + hr
    }
    if (min < 10) {
        min = '0' + min
    }
    if (sec < 10) {
        sec = '0' + sec
    }

    var currentday = yyyy + "-" + mm + "-" + dd;
    return currentday;
}


function getDateTime(date) {
    if (date)
        return date.substring(0, 10) + ' ' + date.substring(11, 19);
    else
        return '';
}

function removeNull(str) {
    if (str == null)
        str = '';
    return str;
}

function removeZero(str) {
    if (str == null || str == '0')
        str = '';
    return str;
}

function getStatus(status) {
    var statuss = { status: capitalizeFirstLetter(status), badge: '', span: capitalizeFirstLetter(status) };
    statuss.badge = status == 'Printed' || status == 'Notice' || status == 'New' || status == 'Ready' ? 'primary'
        : status == 'Captured' || status == 'Reduced' || status == 'issued' || status == 'Saved' || status == 'Exported'  ? 'warning'
            : status == 'Paid' || status == 'Withdrawn' || status == 'Submitted' || status == 'handedin' || status == 'Active' || status == 'Expired'? 'success'
                : status == 'Warrant' || status == 'Deleted' || status == 'Not Found' ? 'danger'
            : 'default';

    statuss.span = '<span class="badge f-12 bg-light-' + statuss.badge + ' rounded-pill f-12">' + statuss.status + '</span>';
    return statuss;
}

function capitalizeFirstLetter(str) {
    return str.replace(/^./, function (char) {
        return char.toUpperCase();
    });
}

function substring(str,len) {
    if (str != null) {
        if (str.length > len)
            str = str.substring(0, len)+'...';
    }

    return removeNull(str);
}



//const dataTable = new simpleDatatables.DataTable('#tblNotice', {
//    sortable: false,
//    perPage: 5
//});




function replaceHolders(str, notice) {
    if (str == null)
        str = '';
    else {
        str = str.replaceAll("[VEHREG]", notice.infringement_notice_registration);
        str = str.replaceAll("[VEHMAKE]", notice.infringement_notice_vehicle_make);
        str = str.replaceAll("[VEHMODEL]", notice.infringement_notice_vehicle_model);
    }
    return str;
}
function removeHolders(str) {
    if (str == null)
        str = '';
    else {
        str = str.replaceAll("[VEHREG]",'');
        str = str.replaceAll("[VEHMAKE]", '');
        str = str.replaceAll("[VEHMODEL]",'');
    }
    return str;
}


function toPercent(total, value) {
    if (total > 0)
        return (parseFloat(100 * value / total).toFixed(2)) + '%';
    else
        return '0%';
}
